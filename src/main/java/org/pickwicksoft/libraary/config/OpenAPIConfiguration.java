package org.pickwicksoft.libraary.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SecurityScheme(
    name = "basicAuth", // can be set to anything
    type = SecuritySchemeType.HTTP,
    scheme = "basic"
)
class OpenAPIConfiguration {}
