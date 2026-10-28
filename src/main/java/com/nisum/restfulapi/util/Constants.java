package com.nisum.restfulapi.util;

public class Constants {

    //********************** ERROR MESSAGES **********************//
    public static final String BLANK_NAME = "El nombre no debe ser nulo o vacío";
    public static final String REGEX_MAIL = "El correo no tiene una estructura válida";
    public static final String BLANK_MAIL = "El correo no debe ser nulo o vacío";
    public static final String EXIST_MAIL = "El correo ya está registrado";
    public static final String REGEX_PASSWORD = "El password no tiene una estructura válida";
    public static final String BLANK_PASSWORD = "El password no debe ser nulo o vacío";
    public static final String EMPTY_PHONES = "Debe tener al menos un teléfono";
    public static final String INVALID_JSON = "El cuerpo de la petición no es un JSON válido";
    public static final String BAD_REQUEST = "La solicitud no es válida";
    public static final String NOT_FOUND = "Recurso no encontrado";
    public static final String METHOD_NOT_ALLOWED = "Método HTTP no permitido";
    public static final String NOT_ACCEPTABLE = "La API solo retorna application/json";
    public static final String UNSUPPORTED_MEDIA_TYPE = "La API solo acepta application/json";
    public static final String INTERNAL_ERROR = "Error interno del servidor";

}
