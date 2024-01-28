# Proyecto Sobel Android

## Resumen
Esta aplicación Android permite a los usuarios seleccionar una imagen de la biblioteca de su dispositivo, aplicar un filtro Sobel y mostrar la imagen filtrada en la pantalla.

Para realizar esta aplicación, se ha añadido como submódulo el repositorio [image-ps](https://github.com/NikoConn/image-ps), para llamar a la función sobel a través de JNI.

Este proyecto se ha realizado utilizando [Android Studio](https://developer.android.com/studio).

## Capturas de Pantalla
<img src="https://github.com/NikoConn/android-sobel-ps/assets/36891809/483a2abb-1cfe-4b4f-87f5-dd711990b559" width=300>

## Clonar proyecto
Dado que el proyecto contiene un submódulo, debe clonarse de la siguiente forma:

```
git clone --recursive git@github.com:NikoConn/android-sobel-ps.git
```

## Uso
Puedes probar la aplicación instalando el apk incluido en la página de [releases](https://github.com/NikoConn/android-sobel-ps/releases/tag/v0.1).
