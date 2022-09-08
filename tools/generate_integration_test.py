#!/usr/bin/python3
import os
import sys
import re


def open_template():
    template = ""

    with open(os.path.join(os.path.dirname(__file__), "IT.template")) as f:
        template = f.read()

    return template

def gen_test(entity_name: str, api_url: str):
    template = open_template()

    template = template.replace("$ENTITY$", entity_name)
    template = template.replace("$ENTITY_SMALL$", entity_name.lower())
    template = template.replace("$API_URL$", api_url)
    template = template.replace("$ATTRIBUTS$", gen_attributs(entity_name))
    attr_setter = gen_attribut_setters(entity_name)
    template = template.replace("$ATTRIBUTS_DEFAULT$", attr_setter[0])
    template = template.replace("$ATTRIBUTS_UPDATED$", attr_setter[1])
    asserts = gen_attributs_assert(entity_name)
    template = template.replace("$ATTRIBUTS_ASSERT$", asserts[0])
    template = template.replace("$ATTRIBUTS_ASSERT_UPDATED$", asserts[1])
    template = template.replace("$MVC_ASSERT$", gen_mock_mvc_assert(entity_name)[0])
    template = template.replace("$MVC_ASSERT_ONE$", gen_mock_mvc_assert(entity_name)[1])

    return template

def gen_attributs(entity_name: str):
    attrs = []
    with open(os.path.join(os.path.dirname(__file__), "../src/main/java/org/pickwicksoft/libraary/domain", f"{entity_name}.java")) as f:
        attrs = re.findall("@Column\(name = \"[a-zA-Z_-]*\", nullable = [a-zA-Z_-]*\)\n    private [a-zA-Z_-]* [a-zA-Z_-]*", f.read())
    attrs = [attr.split("\n")[1] for attr in attrs]        
    attrs = [str(attr).removeprefix("    private ") for attr in attrs]
    attrs = [attr.split(" ") for attr in attrs]
    
    jattrs = ""
    for attr in attrs:
        jattrs += f"""
    private static final {attr[0]} DEFAULT_{attr[1].upper()} = {"AAA" if attr[0] == "String" else 0};
    private static final {attr[0]} UPDATED_{attr[1].upper()} = {"BBB" if attr[0] == "String" else 1};""".replace("AAA", "\"AAA\"").replace("BBB", "\"BBB\"")

    return jattrs

def gen_attribut_setters(entity_name: str):
    attrs = []
    with open(os.path.join(os.path.dirname(__file__), "../src/main/java/org/pickwicksoft/libraary/domain", f"{entity_name}.java")) as f:
        attrs = re.findall("@Column\(name = \"[a-zA-Z_-]*\", nullable = [a-zA-Z_-]*\)\n    private [a-zA-Z_-]* [a-zA-Z_-]*", f.read())
    attrs = [attr.split("\n")[1] for attr in attrs]        
    attrs = [str(attr).removeprefix("    private ") for attr in attrs]
    attrs = [attr.split(" ") for attr in attrs]
    
    jattrs_default = ""
    for attr in attrs:
        member = attr[1]
        member = member[0].upper() + member[1:]
        jattrs_default += f"""
    {entity_name.lower()}.set{member}(DEFAULT_{attr[1].upper()});
    """

    jattrs_updated = ""
    for attr in attrs:
        member = attr[1]
        member = member[0].upper() + member[1:]
        jattrs_updated += f"""
    {entity_name.lower()}.set{member}(UPDATED_{attr[1].upper()});
    """

    return [jattrs_default, jattrs_updated]

def gen_attributs_assert(entity_name: str):
    attrs = []
    with open(os.path.join(os.path.dirname(__file__), "../src/main/java/org/pickwicksoft/libraary/domain", f"{entity_name}.java")) as f:
        attrs = re.findall("@Column\(name = \"[a-zA-Z_-]*\", nullable = [a-zA-Z_-]*\)\n    private [a-zA-Z_-]* [a-zA-Z_-]*", f.read())
    attrs = [attr.split("\n")[1] for attr in attrs]        
    attrs = [str(attr).removeprefix("    private ") for attr in attrs]
    attrs = [attr.split(" ") for attr in attrs]
    
    jattrs = ""
    for attr in attrs:
        member = attr[1]
        member = member[0].upper() + member[1:]
        jattrs += f"""
        assertThat(test{entity_name}.get{member}()).isEqualTo(DEFAULT_{attr[1].upper()});"""

    jattrs_updated = ""
    for attr in attrs:
        member = attr[1]
        member = member[0].upper() + member[1:]
        jattrs_updated += f"""
        assertThat(test{entity_name}.get{member}()).isEqualTo(UPDATED_{attr[1].upper()});"""

    return [jattrs, jattrs_updated]


def gen_mock_mvc_assert(entity_name: str):
    attrs = []
    with open(os.path.join(os.path.dirname(__file__), "../src/main/java/org/pickwicksoft/libraary/domain", f"{entity_name}.java")) as f:
        attrs = re.findall("@Column\(name = \"[a-zA-Z_-]*\", nullable = [a-zA-Z_-]*\)\n    private [a-zA-Z_-]* [a-zA-Z_-]*", f.read())
    attrs = [attr.split("\n")[1] for attr in attrs]        
    attrs = [str(attr).removeprefix("    private ") for attr in attrs]
    attrs = [attr.split(" ") for attr in attrs]
    
    jattrs = ""
    for attr in attrs:
        jattrs += f"""    .andExpect(jsonPath("$.[*].{attr[1]}").value(hasItem(DEFAULT_{attr[1].upper()})))
        """
    jattrs += ";"

    jattrs_one = ""
    for attr in attrs:
        jattrs_one += f"""    .andExpect(jsonPath("$.{attr[1]}").value(DEFAULT_{attr[1].upper()}))
        """
    jattrs_one += ";"

    return [jattrs, jattrs_one]

def save_test(entity_name: str, test: str):
    with open(os.path.join(os.getcwd(), f"{entity_name}ResourceIT.java"), "w") as f:
        f.write(test)


"""Command line interface"""
if __name__ == "__main__":
    import argparse

    parser = argparse.ArgumentParser()
    parser.add_argument("entity", help="Name of the entity")
    parser.add_argument("url", help="API URL")

    args = parser.parse_args()

    test = gen_test(args.entity, args.url)
    save_test(args.entity, test)
