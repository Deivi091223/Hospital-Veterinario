# Hospital Veterinario - Evaluación POO en Java

Sistema de consola en Java para gestionar los pacientes de una clínica veterinaria. Permite ingresar perros, gatos y aves exóticas, vacunarlos, asignarles jaula, medicarlos, darles de alta y generar un reporte.

El proyecto se hizo para una evaluación de Programación Orientada a Objetos y usa solo lo básico: clases, herencia, encapsulamiento y vectores de capacidad fija.

## Requisitos

- JDK 17 o superior
- Sin librerías externas, sin archivos ni bases de datos

## Estructura del proyecto

```
Hospital-Veterinario-Java/
├── .gitignore
├── README.md
└── src/
    ├── Paciente.java     Clase base de los pacientes
    ├── Perro.java        Hereda de Paciente (raza, vacunado)
    ├── Gato.java         Hereda de Paciente (nivel de estrés)
    ├── AveExotica.java   Hereda de Paciente (envergadura)
    ├── Jaula.java        Número, tamaño, zona y ocupante
    ├── Hospital.java     Toda la lógica de gestión
    └── Main.java         Menú de consola (único con Scanner y System.out)
```

### Clases

| Clase | Hereda de | Detalle |
|-------|-----------|---------|
| `Paciente` | - | nombre, peso, edad, estado, factorDosis, dosisAcumulada, jaula |
| `Perro` | `Paciente` | raza, vacunado. `factorDosis = 10` |
| `Gato` | `Paciente` | nivelEstres (0 a 10). `factorDosis = 6` |
| `AveExotica` | `Paciente` | envergadura en cm. `factorDosis = 3` |
| `Jaula` | - | número, tamaño (S, M, L), zona (A o B), ocupante |
| `Hospital` | - | `Paciente[10]` con contador y `Jaula[6]` |

Todos los atributos son `private`. Los setters validan y, si el valor es inválido, no modifican el atributo. Las clases del dominio no leen ni imprimen: devuelven `String` y solo `Main` usa `Scanner` y `System.out`.

## Cómo ejecutarlo

### Desde IntelliJ IDEA

1. Abre la carpeta del proyecto con `File → Open`.
2. En `File → Project Structure → Project` elige un JDK 17 o superior.
3. Marca `src` como `Sources Root` (clic derecho en `src → Mark Directory as → Sources Root`).
4. Ejecuta `Main` con el triángulo verde.
5. Escribe los comandos en la pestaña **Run** y pulsa Enter.

### Desde la terminal

```
cd src
javac *.java
java Main
```

El programa no imprime ningún mensaje al arrancar: queda esperando comandos hasta que escribas `salir`.

## Comandos

Cada línea es un comando, con las palabras separadas por espacios y los decimales con punto.

| Comando | Éxito |
|---------|-------|
| `ingresar perro <nombre> <peso> <edad> <raza> <si/no>` | `OK: <nombre> ingresado` (sin vacunar: `OK: <nombre> ingresado en CUARENTENA`) |
| `ingresar gato <nombre> <peso> <edad>` | `OK: <nombre> ingresado` |
| `ingresar ave <nombre> <peso> <edad> <envergadura>` | `OK: <nombre> ingresado` |
| `vacunar <nombre>` | `OK: <nombre> vacunado (pasa a INGRESADO)` |
| `asignar <nombre>` | `OK: <nombre> asignado a jaula <n>` |
| `medicar <nombre> <mg>` | `OK: <nombre> medicado con <mg> mg` |
| `alta <nombre>` | `OK: <nombre> dado de alta` |
| `reporte` | Muestra el estado del hospital (ver formato abajo) |
| `salir` | Termina el programa |

Un comando que no existe responde `ERROR: comando desconocido`.

### Formato del reporte

```
--- REPORTE ---
Pacientes: 3/10
Rocky | Perro | 30.0 kg | INGRESADO | jaula: -
Misifu | Gato | 4.0 kg | INGRESADO | jaula: -
Kiwi | Ave | 0.8 kg | INGRESADO | jaula: -
Jaulas libres: 6
```

## Reglas de negocio

- **Datos válidos:** nombre no vacío; peso mayor que 0 y máximo 80 kg (perro), 15 kg (gato) o 15 kg (ave); edad de 0 a 30; envergadura mayor que 0.
- **Ingreso:** máximo 10 pacientes y nombre único sin distinguir mayúsculas. Se valida en el orden datos, duplicado, lleno.
- **Cuarentena:** un perro no vacunado ingresa en `CUARENTENA`; los demás ingresan como `INGRESADO`. En cuarentena no se asigna jaula ni se medica.
- **Tamaño de jaula según el peso:** menos de 5 kg es `S`, de 5 a 25 kg es `M` y más de 25 kg es `L`. Un ave con envergadura mayor a 60 cm necesita como mínimo `M`.
- **Zona:** perros y gatos solo en zona `A`; aves solo en zona `B`.
- **Asignar:** busca la primera jaula libre que cumpla tamaño y zona. Jaula y paciente quedan enlazados.
- **Dosis:** la dosis máxima por aplicación es `peso × factorDosis`. El acumulado no puede superar 3 veces esa dosis máxima.
- **Medicar:** exige jaula asignada. Cada medicación válida suma los mg al acumulado y pasa al paciente a `EN_TRATAMIENTO`. Si es gato, su estrés sube 2 (tope 10) y con estrés de 8 o más no se le puede medicar.
- **Alta:** solo procede si el estado es `EN_TRATAMIENTO`. Libera la jaula y elimina al paciente del vector, compactándolo.

## Datos iniciales

Al arrancar, `Hospital.cargarDatosIniciales()` carga estos datos (todos sin jaula):

| Paciente | Tipo | Peso (kg) | Edad | Dato propio | Estado |
|----------|------|-----------|------|-------------|--------|
| Rocky | Perro | 30.0 | 4 | labrador, vacunado | INGRESADO |
| Misifu | Gato | 4.0 | 3 | estrés 0 | INGRESADO |
| Kiwi | Ave | 0.8 | 2 | envergadura 70 cm | INGRESADO |

Jaulas: 1 (A, S), 2 (A, M), 3 (A, M), 4 (A, L), 5 (B, S), 6 (B, M).

## Ejemplo de uso

```
asignar Rocky
OK: Rocky asignado a jaula 4
medicar Rocky 100
OK: Rocky medicado con 100 mg
medicar Rocky 400
ERROR: dosis excede el maximo
alta Rocky
OK: Rocky dado de alta
salir
```
