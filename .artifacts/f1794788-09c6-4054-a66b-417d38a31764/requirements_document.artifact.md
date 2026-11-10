# Documento de Requerimientos y Funcionalidades — Twinnify

Este documento detalla todas las características, módulos y funcionalidades implementadas en la aplicación **Twinnify** (anteriormente TwinnDroid), una potente herramienta sin root para clonar, aislar, extraer y personalizar aplicaciones en Android.

---

## 1. Módulo de Clonación y Personalización de Apps

Permite crear copias independientes de aplicaciones instaladas o archivos APK externos, asignándoles su propia identidad y datos.

- **Selección de Origen:**
  - Selección de cualquier aplicación instalada en el dispositivo (filtrando opcionalmente aplicaciones de sistema).
  - Importación directa desde archivos APK externos.
- **Personalización de Identidad:**
  - **Nombre personalizado:** Cambiar el nombre de la app clonada.
  - **Nombre de paquete único (`Package Name`):** Permite aislar los datos y la sesión de la app original (con validación de nombres de paquete y prevención de colisiones).
  - **Icono personalizado:** Uso de iconos originales, iconos preestablecidos o carga de imágenes propias con soporte para insignias (`badges`), texto en insignias y ajustes de color.
  - **Ajustes de color e imagen:** Control de tono (`hue`), saturación, brillo, contraste, tinte, inversión, giro y rotación.
- **Gestión de Permisos:**
  - Visualización del total de permisos que solicita la app.
  - Activación/desactivación selectiva de permisos.
  - Opción rápida para eliminar permisos sensibles a la privacidad.
- **Opciones Avanzadas y Endurecimiento (`Hardening`):**
  - **Modo Debuggable:** Permite depurar el clon mediante adb / Android Studio.
  - **Firma propia:** Cada clon se firma con una clave de firma dedicada para aislar su ID de dispositivo.
  - **Aislamiento avanzado:**
    - Ocultar otras aplicaciones instaladas (para Android 11+).
    - Componentes privados (evita que otras apps abran sus pantallas directamente).
    - Sin almacenamiento compartido (carpeta privada exclusiva).
    - Exclusión de copias de seguridad en la nube y adb.
    - Eliminación de acceso al ID de publicidad.
    - Bloqueo de inicio automático al encender el dispositivo (`No start at boot`).
    - Bloqueo total de internet (`No internet access`).
- **Ciclo de Vida del Clon:**
  - Reconstrucción y reinstalación manteniendo los datos si se usa la misma clave.
  - Desinstalación de clones y limpieza de registros.

---

## 2. Módulo de Espacio Aislado (Sandbox / Work Profile)

Crea y gestiona un perfil de trabajo separado en el dispositivo mediante las APIs nativas de Android, permitiendo ejecutar aplicaciones o copias secundarias con aislamiento estricto.

- **Creación y Gestión del Sandbox:**
  - Creación del perfil de trabajo controlado exclusivamente por la app.
  - Detección de conflictos si otro perfil de trabajo (como Shelter o perfiles corporativos) ya está activo.
  - Estado del sandbox (pausado, activo, no compatible).
- **Adición de Aplicaciones:**
  - Añadir apps originales sin modificar (ideal para aplicaciones protegidas contra clonación).
  - Acceso a copias adicionales mediante clonación en sandbox.
- **Reglas de Seguridad y Privacidad del Sandbox:**
  - **Bloqueo de portapapeles:** Impide compartir texto copiado entre el sandbox y el perfil personal.
  - **Bloqueo de compartir archivos:** Evita que apps personales envíen archivos o enlaces al sandbox.
  - **Bloqueo de contactos:** Impide que apps personales accedan a contactos guardados en el sandbox y viceversa.
  - **Permisividad de desarrollo:** Activación opcional de adb y Shizuku dentro del sandbox.
- **Control de Permisos por App en Sandbox:**
  - Interruptores individuales para bloquear permisos de cámara, micrófono, ubicación, contactos, calendario, teléfono, SMS, archivos, dispositivos cercanos y sensores corporales.
- **Auto-congelación (`Auto-freeze`):**
  - Congela automáticamente las apps del sandbox al bloquear la pantalla del teléfono.
  - Retraso configurable en segundos.
  - Excepción inteligente: no se congela si hay audio reproduciéndose en segundo plano.
  - Atajos de pantalla de inicio para congelar todas las apps del sandbox de un toque.
- **Intercambio de Archivos y Utilidades:**
  - Envío de archivos hacia el interior del sandbox (`Send files in`) y extracción hacia el teléfono (`Bring files out`) utilizando la carpeta `Download/Twinnify`.
  - Stub de pago NFC (`NFC payment stub`) para permitir el funcionamiento de apps de pago contactless dentro del sandbox.
  - Reseteo de datos de apps en sandbox y desinstalación controlada.

---

## 3. Módulo de Extracción de APKs (`APK Extractor`)

- **Extracción de Aplicaciones Instaladas:**
  - Extrae cualquier aplicación del sistema o de usuario junto con sus splits (paquetes divididos).
  - Soporte para formatos de salida: `.apk`, `.apks`, `.xapk` y `.apkm`.
- **Fusión de Splits (`Merged APK`):**
  - Combina paquetes divididos en un único archivo APK instalable.
- **Compartir y Guardar:**
  - Guardado en almacenamiento y opción directa para compartir el archivo extraído.

---

## 4. Módulo de Actualizaciones y Verificación

- **Monitoreo de Aplicaciones Originales:**
  - Comprobación automática cada pocas horas (`UpdateWorker`) para verificar si las apps originales de los clones se han actualizado.
  - Notificaciones al usuario ante nuevas versiones.
- **Auto-actualización en Segundo Plano:**
  - Reconstrucción y reinstalación silenciosa de clones (requiere Shizuku).
- **Actualizaciones de Twinnify:**
  - Verificación de nuevas versiones de la propia aplicación a través de GitHub.

---

## 5. Módulo de Copia de Seguridad y Restauración (`Backup & Restore`)

- **Exportación de Datos:**
  - Guarda en un archivo comprimido único la lista de clones, clave de firma, APKs importados, iconos personalizados y configuraciones generales.
- **Restauración:**
  - Restaura la configuración completa en el mismo dispositivo tras una reinstalación o en un teléfono nuevo, permitiendo que los clones sigan actualizándose sin perder el hilo.

---

## 6. Módulo de Apariencia, Temas y Efectos Visuales

Ofrece un sistema de personalización visual altamente avanzado e inmersivo.

- **Temas y Modos:**
  - Soporte para tema del sistema, modo claro, modo oscuro y **Material You** (colores dinámicos basados en el fondo de pantalla).
  - Modo negro puro (`Pure black`) optimizado para pantallas OLED.
- **Presets de Color:**
  - 10+ temas predefinidos: *Midnight, Mint, Ocean, Sunset, Rose, Mono, Lavender, Cyberpunk, Nord, Dracula*.
- **Temas Personalizados ("My themes"):**
  - Selector de colores personalizados por aspecto (acento, secundario, terciario, fondo, tarjetas, degradados de cabecera).
  - Guardado y carga de temas propios.
- **Animaciones y Efectos de Fondo (`Background & effects`):**
  - Animaciones fluidas en tiempo real: *Space, Particles, Circles, Rings, Mesh, Shapes, Snow, Grid, Lava lamp, Aurora, Waves, Fireflies, Bokeh, Meteors, Random*.
  - Efecto Parallax (inclinación del fondo al mover el teléfono).
  - Tarjetas de vidrio translúcidas (`Glass cards`) con ajuste de opacidad y desenfoque (`Blur` compatible con Android 12+).
  - Tarjetas flotantes y contornos refinados.
- **Tamaño de Texto y Accesibilidad:**
  - Escalado personalizado del texto respecto al sistema.
- **Selector de Icono de la App:**
  - Cambio dinámico del icono principal de Twinnify en el cajón de aplicaciones mediante alias de actividades.

---

## 7. Integración con Shizuku y Permisos del Sistema

- **Soporte de Shizuku:**
  - Autorización mediante Shizuku (compatible con depuración inalámbrica sin root).
  - Permite la instalación de clones de forma totalmente silenciosa y sin diálogos de confirmación del sistema.
  - Habilita las actualizaciones automáticas en segundo plano.
- **Gestión de Accesos Especiales:**
  - Acceso a todos los archivos, instalación de fuentes desconocidas y superposición sobre otras aplicaciones (`Display over other apps`).
