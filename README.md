Compilación:

Al ser programado en Java, es necesario que descarguen como mínimo la SDK 12.
Al ser un proyecto Maven, para compilar y crear el .jar se debe usar el comando 'mvn clean package', el cual crea el .jar dentro de la carpeta 
sia_tp1/target.
En el caso de tener los archivos de configuracion level.txt y config.json ya en esa carpeta, el comando anterior los borrará y deben crearse de nuevo.

Ejecución:

Dentro de la carpeta sia_tp1/target se encuentra el ejecutable sia_tp1-1.0-SNAPSHOT.jar, las dependencias, y los archivos de configuración level.txt y config.json.
Dentro de level.txt deben ingresar el nivel al que quieran calcularle la solución, teniendo en cuenta que:
  - '#' es una pared
  - '.' es un objetivo
  - '@' es el jugador
  - '$' es una caja
  - ' ' es un espacio en donde el jugador y cajas pueden moverse
 
Dentro de config.json se ingresan los parametros para la búsqueda:
 - algorithm: se ingresa el nombre del algoritmo de búsqueda a utilizar ["BFS","DFS","IDDFS","A*","IDA*","GGS"]
 - heuristic: se ingresa el nombre de la heuristica a utilizar en búsquedas informadas ["goalCount","SMD","MML"]
 - maxIter: se ingresa el numero máximo de iteraciones en caso de utilizar IDDFS
 - deadlockCheck: booleano que indica si se debe verificar que una caja quede en una casilla de la que no pueda moverse más, reduciendo bastante la cantidad de nodos        explorados. En las búsquedas informadas es siempre 'true', mientras que en las desinformadas, aunque por defecto es 'false', puede ser 'true'.

Para ejecutar el programa puede hacerle doble click al archivo .jar o dentro de la consola, colocandose en la carpeta sia_tp1/target e ingresar el comando: java -jar ./sia_tp1-1.0-SNAPSHOT.jar


Ejemplos de configuraciones de ejecución:
{
  "algorithm":"BFS",
  "deadlockCheck":"true"
}
,
{
  "algorithm":"A*",
  "heuristic":"MML"
}
,
{
  "algorithm":"IDDFS",
  "maxIter":"100",
  "heuristic":"MML"
}
