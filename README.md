# Plataforma de Gestion de Energia Iinteligente

### **⚡ Sistema de Gestión de Energía Inteligente (SGEI).**

Una red eléctrica inteligente necesita implementar un sistema que permita registrar distintas operaciones de transferencia de energía sobre baterías de almacenamiento (nodos) y ejecutarlas en distintos momentos. Además, el sistema deberá evolucionar para que otros componentes periféricos de monitoreo puedan reaccionar automáticamente cuando ocurran variaciones en la carga de una batería.

#### **👨‍🏫 Primera Parte \- Orquestación de Energía.**

**📋 Requerimientos.**

**🔋 Batería de Almacenamiento.**  
De cada batería de almacenamiento en la red se conoce:

* Identificador de la Batería.  
* Nivel de Energía Actual (en kWh).

Una batería debe permitir:

* Consultar su nivel de energía actual.  
* Cargar energía (incrementar kWh).  
* Consumir energía (reducir kWh).

**⚙️ Operaciones de Transferencia.**   
El sistema debe soportar inicialmente las operaciones de **Carga** y **Consumo**. Cada operación encapsula toda la información necesaria para ejecutarse, conociendo la batería específica sobre la que actúa y la cantidad de energía (kWh) involucrada.

**📋 Controlador de Operaciones.**  
La plataforma cuenta con un componente responsable de administrar las transferencias de energía. Este componente debe permitir:

* Ejecutar una operación de forma inmediata.  
* Registrar operaciones pendientes y agruparlas en una "Rutina" (lote).  
* Ejecutar todas las operaciones contenidas en una rutina.  
* Vaciar el listado de operaciones pendientes de una rutina luego de ejecutarlas.

**¿Qué pasa si queremos deshacer una operación?**

* ¿Es posible revertir una operación de carga o consumo ya ejecutada?  
* ¿De quién es la responsabilidad de saber revertir el impacto de la operación en la batería y cómo se implementa para una operación individual o para una rutina completa?

**⚠️ Robustez y Control de Errores.**  
El sistema debe garantizar la consistencia de los datos mediante el manejo de dos situaciones anómalas:

1. **Valores Inválidos:** Si se intenta instanciar una operación (Carga o Consumo) con un valor menor o igual a **0 kWh**, el sistema debe lanzar un error que denote un uso indebido de la lógica. Dado que el código cliente no debería generar estas llamadas si está bien programado, **el compilador no debe obligar a capturar esta falla explícitamente**.  
2. **Límite de Reserva:** Se admite un uso de reserva física (*overdraft*) en la batería de hasta **\-5,000 kWh**. Si un consumo intenta forzar a la batería por debajo de este límite absoluto, se debe lanzar un error de regla de negocio. Al ser una situación operativa anómala pero "esperable", **el diseño debe obligar en tiempo de compilación** a que quien invoque la operación se haga cargo de manejar la situación.

**🔄 Transaccionalidad.**

* Si al ejecutar una **Rutina** que contiene múltiples operaciones, una de ellas falla por superar el límite de reserva, la rutina debe interrumpirse. Inmediatamente, el sistema debe revertir de forma automática únicamente las operaciones de esa misma rutina que ya se habían ejecutado con éxito, devolviendo la batería al estado exacto que tenía antes de iniciar el lote.

**⚙️ Restricciones de Diseño (Primera Parte).**

* El controlador no debe conocer cómo se implementa internamente cada operación.  
* El sistema debe permitir tratar de manera uniforme tanto a una operación individual como a una rutina (que contiene múltiples operaciones), de modo que el controlador pueda ejecutarlas usando la misma interfaz.  
* Debe ser posible incorporar nuevos tipos de operaciones de red sin modificar el código del controlador.  
* Evitar soluciones basadas en condicionales (if / switch) para distinguir tipos de operaciones.  
* Las operaciones deben encapsularse de forma tal que puedan encolarse y cada una sea capaz de deshacer su propia transformación sobre la batería.

####  **👨‍🏫 Segunda Parte \- Notificaciones Automáticas.**

La plataforma desea incorporar sistemas periféricos que reaccionen automáticamente a las variaciones de energía.

**📢 Mecanismo de Suscripción** .  
Cada vez que una batería reciba una carga o consumo exitoso, los sistemas interesados deben ser informados automáticamente. La batería debe permitir:

* Registrar sistemas interesados de forma dinámica.  
* Eliminar sistemas interesados.  
* Avisar a todos los inscriptos cuando ocurra un movimiento de energía.

**🔍 Registro Central de Auditoría.**   
Registra todas las alteraciones exitosas realizadas sobre las baterías.  
**📱 Notificaciones al Administrador.**   
Informa al responsable técnico cada vez que ocurre una variación de energía en su batería específica (ej: *"Se han cargado 2000 kWh en su batería"*).

**🚨 Alarma de Reserva Crítica.**   
Detecta cuándo una batería queda por debajo de su nivel de tolerancia (cero kWh) tras una operación exitosa y advierte sobre el uso del límite de reserva.

**⚙️ Restricciones de Diseño (Segunda Parte).**

* La batería no debe conocer los detalles de las clases concretas de los sistemas periféricos (Auditoría, Notificaciones, etc.).  
* Debe ser posible incorporar nuevos sistemas interesados sin modificar el código estructural de la Batería.  
* Un mismo cambio de energía puede disparar múltiples reacciones concurrentes en diferentes sistemas.  
* Evitar el uso de condicionales (if / switch / instanceof) en la batería para identificar o filtrar a quién se le está avisando.

#### 

#### **⏱️ Condiciones de Evaluación.**

* **Tiempo máximo de resolución:** 2 horas y 30 minutos reloj.  
* **Metodología:** Desarrollo Guiado por Pruebas (TDD) y refactors sucesivos.  
* **Métrica de éxito:** Se exige alcanzar una cobertura de código (Code Coverage) de al menos un **75%**, demostrando mediante tests el correcto funcionamiento del modelo, las estructuras arquitectónicas implícitas y el comportamiento transaccional ante las fallas descritas.