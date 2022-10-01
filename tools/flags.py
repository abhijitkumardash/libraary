import os


def get_flag_from_language_code(language_code):
    """Return the flag for a language code. Return a flag as character.
    e.g. 'de' -> '🇩🇪' or 'en' -> '🇬🇧' Compose them using the + operator.
    """
    return chr(0x1F1E6 + ord(language_code[0]) - ord('a')) + chr(0x1F1E6 + ord(language_code[1]) - ord('a'))
    

# Add the flags to the third column of language csv file. The first column is the language code.
# The second column is the language name. The third column is the flag.

if __name__ == '__main__':
    """Main function"""
    with open(os.path.join(os.path.dirname(__file__), '../src/main/resources/config/liquibase/data/language.csv'), 'r') as file:
        lines = file.readlines()
        for i in range(len(lines)):
            if i != 0:
                lines[i] = lines[i].strip() + ';' + get_flag_from_language_code(lines[i].split(';')[0] if lines[i].split(';')[0].find("_") == -1 else lines[i].split(';')[0].split("_")[0]) + '\n'
    with open(os.path.join(os.path.dirname(__file__), '../src/main/resources/config/liquibase/data/language2.csv'), 'w') as file:
        file.writelines(lines)