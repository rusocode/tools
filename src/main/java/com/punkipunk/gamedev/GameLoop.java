package com.punkipunk.gamedev;

/**
 * <h1>Game Loop</h1>
 * El <b>Game Loop</b> (bucle de juego) es un concepto fundamental en la programacion de videojuegos que se refiere a la
 * estructura repetitiva que controla el flujo de ejecucion de un juego. Es una parte esencial del ciclo de vida de un
 * videojuego y esta diseñado para manejar la logica y la simulacion del juego en cada iteracion.
 * <p>
 * En esencia, el Game Loop es una serie de instrucciones que se ejecutan una y otra vez mientras el juego esta en
 * funcionamiento. Cada iteracion del bucle representa un "frame" o cuadro del juego y se compone generalmente de tres
 * fases principales:
 * <ol>
 * <li><i>Procesamiento de Entradas</i>: En esta fase, se capturan las entradas del jugador, como movimientos, clics de
 * botones y otros comandos. Estas entradas se utilizan para actualizar el estado del juego y responder a las acciones
 * del jugador.
 * <li><i>Actualizacion de la Logica</i>: En esta fase, se actualiza la logica del juego, que incluye la simulacion de
 * la fisica, la inteligencia artificial, las colisiones y cualquier otro aspecto que cambie con el tiempo. Aqui es
 * donde ocurren los calculos que determinan el estado del juego en el siguiente frame.
 * <li><i>Renderizacion</i>: En esta fase, se crea la representacion visual del juego en funcion del estado actual. Los
 * elementos graficos, como personajes, fondos y efectos, se dibujan en la pantalla para que el jugador los vea.
 * </ol>
 * El Game Loop se repite a una tasa constante, generalmente determinada por la tasa de fotogramas (FPS) a la que se
 * renderiza el juego. Por ejemplo, si el juego tiene un FPS objetivo de 60, el Game Loop se ejecutara aproximadamente
 * 60 veces por segundo.
 * <p>
 * El diseño y la implementacion del Game Loop son criticos para lograr una experiencia de juego fluida y coherente. Un
 * buen Game Loop debe ser eficiente y balancear correctamente el tiempo dedicado a cada fase, asegurando que la
 * actualizacion de la logica y la renderizacion se realicen de manera adecuada para proporcionar una experiencia de
 * juego suave y agradable.
 * <br><br>
 * <h2>¿Que tan rapido gira el bucle del juego? (Un mundo fuera de tiempo)</h2>
 * Si medimos la rapidez con la que se repite el Game Loop en terminos de tiempo real, obtenemos los {@link FPS}.
 * <p>
 * Con el bucle crudo que tenemos ahora, donde simplemente se cicla tan rapido como puede, dos factores determinan la
 * velocidad de fotogramas. El primero es <i>cuanto trabajo tiene que hacer cada frame</i>. La fisica compleja, un
 * monton de objetos de juego y muchos detalles graficos mantendran ocupados a la CPU y GPU, y llevara mas tiempo
 * completar un frame. Esto tambien se explica en {@link Tick}.
 * <p>
 * El segundo es <i>la velocidad de la plataforma subyacente</i>. Los chips mas rapidos procesan mas codigo en la
 * misma cantidad de tiempo. Multiples nucleos, GPU, hardware de audio dedicado y el programador del sistema operativo
 * afectan cuanto se hace en un solo paso.
 * <br><br>
 * <h2>Segundo por segundo</h2>
 * Los juegos mas antiguos fueron cuidadosamente codificados para hacer el trabajo suficiente en cada frame para que el
 * juego se ejecutara a la velocidad que querian los desarrolladores. Pero si intentara jugar ese mismo juego en una
 * maquina mas rapida o mas lenta, entonces el juego en si se aceleraria o se ralentizaria.
 * <p>
 * En estos dias, sin embargo, pocos desarrolladores tienen el lujo de saber exactamente en que hardware se ejecutara
 * su juego. En cambio, nuestros juegos deben adaptarse de manera inteligente a una variedad de dispositivos.
 * <p>
 * Este es el otro trabajo clave de un bucle de juego: <i>ejecuta el juego a una velocidad constante a pesar de las
 * diferencias en el hardware subyacente</i>.
 * <p>
 * <h2>Codigo de muestra</h2>
 * El bucle del juego impulsa la IA, el renderizado y otros sistemas de juego, pero esos no son el objetivo del patron
 * en si, por lo que aqui solo llamaremos a metodos ficticios.
 * <br><br>
 * <h3>Corre, corre, tan rapido como puedas</h3>
 * Este el ciclo de juego mas simple posible:
 * <pre>{@code
 * while (true)
 * {
 *   processInput();
 *   update();
 *   render();
 * }
 * }</pre>
 * El problema con esto es que no tienes control sobre que tan rapido corre el juego. En una maquina rapida, ese bucle
 * girara tan rapido que los usuarios no podran ver lo que esta pasando. En una maquina lenta, el juego se arrastrara.
 * Si tienes una parte del juego que tiene mucho contenido o tiene mas IA o fisica, el juego en realidad se jugara mas
 * lento alli.
 * <br><br>
 * <h3>Tomando una pequeña siesta</h3>
 * La primera variacion que veremos agrega una solucion simple. Digamos que quieres que tu juego se ejecute a 60 FPS.
 * Eso le da alrededor de 16 milisegundos por frame. Siempre que pueda realizar todo el procesamiento y renderizado de
 * su juego de manera confiable en menos de ese tiempo, puede ejecutar a una velocidad de fotogramas constante. Todo lo
 * que hace es procesar el frame y luego <i>esperar</i> hasta que sea el momento para el siguiente. Esta solucion se
 * aplica utilizando el metodo {@code sleep()}. Ver la imagen <i>"Game Loop 1.png"</i>.
 * <p>
 * El codigo se parece un poco a esto:
 * <pre>{@code
 * while (true)
 * {
 *   double start = getCurrentTime();
 *   processInput();
 *   update();
 *   render();
 *
 *   sleep(start + MS_PER_FRAME - getCurrentTime());
 * }
 * }</pre>
 * <p>
 * Aqui se asegura de que usando sleep(), el juego no se ejecute demasiado rapido si procesa un frame rapidamente.
 * Aunque esto no ayuda si su juego se ejecuta demasiado lento. Si se tarda mas de 16 ms en actualizar y renderizar el
 * frame, el tiempo de suspension se vuelve negativo.
 * <p>
 * Puede solucionar esto haciendo menos trabajo en cada frame: reduzca los graficos y simplifique la IA. Pero eso afecta
 * la calidad del juego para todos los usuarios, incluso los de maquinas rapidas.
 * <br><br>
 * <h3>Un pequeño paso, un paso gigante</h3>
 * Probemos algo un poco mas sofisticado. El problema que tenemos basicamente se reduce a:
 * <ol>
 * <li>Cada actualizacion avanza el tiempo de juego en cierta cantidad.
 * <li>Se necesita una cierta cantidad de tiempo <i>real</i> para procesar eso.
 * </ol>
 * Si el paso dos lleva mas tiempo que el paso uno, el juego se ralentiza. Si se necesitan mas de 16 ms de procesamiento
 * para avanzar el tiempo del juego en 16 ms, es imposible que pueda seguir el ritmo. Pero si podemos avanzar el juego
 * en <i>mas</i> de 16 ms de tiempo de juego en un solo paso, entonces podemos actualizar el juego con menos frecuencia
 * y aun asi mantener el ritmo.
 * <p>
 * Entonces, la idea es elegir un timestep para avanzar en funcion de cuanto tiempo real paso desde el ultimo cuadro.
 * Cuanto mas tiempo toma el cuadro, mas pasos toma el juego. Siempre se mantiene en tiempo real porque se necesitaran
 * pasos cada vez mas grandes para llegar alli. Llaman a esto un timestep <i>variable</i> o <i>fluido</i>.
 * Parece que:
 * <pre>{@code
 * double lastTime = getCurrentTime();
 * while (true)
 * {
 *   double current = getCurrentTime();
 *   double elapsed = current - lastTime;
 *   processInput();
 *   update(elapsed);
 *   render();
 *   lastTime = current;
 * }
 * }</pre>
 * En cada cuadro, determinamos cuanto tiempo <i>real</i> paso desde la ultima actualizacion del juego ({@code elapsed}).
 * Cuando actualizamos el estado del juego, lo pasamos. El motor es entonces responsable de hacer avanzar el mundo del
 * juego por esa cantidad de tiempo.
 * <p>
 * Digamos que tienes una bala disparando a traves de la pantalla. Con un timestep fijo, en cada cuadro, lo movera de
 * acuerdo con su velocidad. Con un timestep variable, <i>escala esa velocidad por el tiempo transcurrido</i>. A medida
 * que aumenta el timestep, la bala se mueve mas lejos en cada fotograma. Esa bala atravesara la pantalla en la <i>misma</i>
 * cantidad de tiempo <i>real</i>, ya sean veinte pequeños pasos rapidos o cuatro grandes pasos lentos. Esto se ve como
 * un ganador:
 * <ul>
 * <li>El juego se juega a un ritmo constante en diferentes hardware.
 * <li>Los jugadores con maquinas mas rapidas son recompensados con un juego mas fluido.
 * </ul>
 * Pero, por desgracia, hay un problema grave al acecho: hemos hecho que el juego sea inestable y no determinista. He
 * aqui un ejemplo de la trampa que nos hemos tendido a nosotros mismos:
 * <p>
 * Digamos que tenemos un juego en red para dos jugadores y Fred tiene una maquina de juego increible, mientras que
 * George esta usando la PC antigua de su abuela. Esa bala antes mencionada esta volando a traves de sus dos pantallas.
 * En la maquina de Fred, el juego se ejecuta super rapido, por lo que cada timestep es pequeño. Metemos como 50
 * fotogramas en el segundo que tarda la bala en atravesar la pantalla. La maquina del pobre George solo puede caber en
 * unos cinco marcos.
 * <p>
 * Esto significa que en la maquina de Fred, el motor de fisica actualiza la posicion de la bala 50 veces, pero la de
 * George solo lo hace cinco veces. La mayoria de los juegos usan numeros de punto flotante y estan sujetos a <i>errores
 * de redondeo</i>. Cada vez que agrega dos numeros de punto flotante, la respuesta que obtiene puede ser un poco
 * incorrecta. La maquina de Fred esta haciendo diez veces mas operaciones, por lo que acumulara un error mayor que
 * George. La <i>misma</i> bala terminara en <i>diferentes lugares</i> de sus maquinas. Este es solo un problema
 * desagradable que puede causar un timestep variable.
 * <p>
 * Esta inestabilidad es tan mala que este ejemplo solo esta aqui como una advertencia y para llevarnos a algo mejor...
 * <br><br>
 * <h3>Jugar a ponerse al dia</h3>
 * Una parte del motor que normalmente <i>no</i> se ve afectada por un timestep variable es el renderizado. Dado que el
 * motor de renderizado captura un instante en el tiempo, no importa cuanto tiempo haya avanzado desde el ultimo.
 * Representa las cosas dondequiera que esten en ese momento.
 * <p>
 * Podemos usar este hecho a nuestro favor. <i>Actualizaremos</i> el juego usando un timestep fijo porque eso hace que
 * todo sea mas simple y mas estable para la fisica y la IA. Pero permitiremos flexibilidad cuando <i>rendericemos</i>
 * para liberar algo de tiempo de procesador.
 * <p>
 * Es asi: ha transcurrido una cierta cantidad de tiempo real desde el ultimo turno del ciclo del juego. Esta es la
 * cantidad de tiempo de juego que necesitamos simular para que el "ahora" del juego se ponga al dia con el del jugador.
 * Hacemos eso usando una <i>serie</i> de pasos de tiempo <i>fijo</i>. El codigo se parece un poco a:
 * <pre>{@code
 * double previous = getCurrentTime();
 * double lag = 0.0;
 * while (true)
 * {
 *   double current = getCurrentTime();
 *   double elapsed = current - previous;
 *   previous = current;
 *   lag += elapsed;
 *
 *   processInput();
 *
 *   while (lag >= MS_PER_UPDATE)
 *   {
 *     update();
 *     lag -= MS_PER_UPDATE;
 *   }
 *
 *   render();
 * }
 * }</pre>
 * Hay algunas piezas aqui. Al comienzo de cada cuadro, actualizamos el {@code lag} en funcion de la cantidad de tiempo
 * real transcurrido. Esto mide que tan atrasado esta el reloj del juego en comparacion con el mundo real. Luego tenemos
 * un ciclo interno para actualizar el juego, un paso fijo a la vez, hasta que se pone al dia. Una vez que estamos al
 * dia, renderizamos y empezamos de nuevo. Ver la imagen <i>"Game Loop 2".</i>
 * <p>
 * Tenga en cuenta que el timestep aqui ya no es la velocidad de fotogramas <i>visible</i>. {@code MS_PER_UPDATE}
 * es solo la <i>granularidad</i> que usamos para actualizar el juego. Cuanto mas corto sea este paso, mas tiempo de
 * procesamiento se necesita para ponerse al dia en tiempo real. Cuanto mas largo es, mas entrecortado es el juego. Lo
 * ideal es que sea bastante corto, a menudo mas rapido que 60 FPS, para que el juego simule con alta fidelidad en
 * maquinas rapidas.
 * <p>
 * Pero tenga cuidado de no hacerlo <i>demasiado</i> corto. Debe asegurarse de que el timestep sea mayor que el tiempo
 * que lleva procesar una {@code update()}, incluso en el hardware mas lento. De lo contrario, tu juego simplemente no
 * puede ponerse al dia.
 * <p>
 * Afortunadamente, nos hemos comprado un respiro aqui. El truco es que hemos <i>sacado el renderizado del ciclo de
 * actualizacion</i> (interpolacion de renderizado o liberar la fisica). Eso libera un monton de tiempo de CPU. El
 * resultado final es que el juego <i>simula</i> a una velocidad constante utilizando pasos de tiempo fijos y seguros en
 * una variedad de hardware. Es solo que la <i>ventana visible</i> del jugador en el juego se vuelve mas entrecortada en
 * una maquina mas lenta.
 * <p>
 * Por ejemplo, Doom renderiza frames lo mas rapido posible. Pero no ejecuta la logica del juego en cada cuadro que
 * dibuja, solo interpola el estado mundial entre las actualizaciones del juego anteriores y actuales.
 * <br><br>
 * Recursos:
 * <a href="https://gafferongames.com/post/fix_your_timestep/">Fix Your Timestep!</a>
 * <a href="http://gameprogrammingpatterns.com/game-loop.html">Game Loop</a>.
 * <a href="https://gamedev.stackexchange.com/questions/1589/when-should-i-use-a-fixed-or-variable-time-step">¿Cuando debo usar un timestep fijo o variable?</a></h2>
 * <a href="https://gamedev.stackexchange.com/questions/132831/what-is-the-point-of-update-independent-rendering-in-a-game-loop">¿Cual es el punto de actualizar independientemente del renderizado?</a>
 * <a href="https://www.reddit.com/r/gamedev/comments/22k6pl/fixed_time_step_vs_variable_time_step/">Timestep fijo vs timestep variable</a>
 * <a href="https://www.reddit.com/r/gamedev/comments/8sci48/should_i_be_using_a_fixed_timestep_or_delta_time/">¿Debo usar un timestep fijo o un tiempo delta?</a>
 * <a href="https://stackoverflow.com/questions/57710138/why-gameloops-render-more-times-than-updating#:~:text=A%20tick%20is%20whenever%20game,to%20a%20redstone%20circuit%20updating">¿Por que el Game Loop se renderiza mas veces de las que se actualiza?</a>
 * <a href="https://www.youtube.com/watch?v=pctGOMDW-HQ">TIMESTEPS and DELTA TIME | Game engine series</a>
 */

public class GameLoop implements Runnable {

    private Thread thread;
    private boolean running, stopped;

    /* Otra forma de detener el loop es declarando running como una variable volatil. La palabra clave volatil prohibe
     * que una variable se copie a la memoria local; la variable permanece en la memoria principal. Por lo tanto, el
     * cambio en esa variable por parte de un subproceso sera visto por todos los otros. */
    // private volatile boolean running;

    @Override
    public void run() {

        GameTimer gameTimer = new GameTimer();
        int ticks = 0, frames = 0;
        boolean shouldRender = false;
        long timer = System.currentTimeMillis();

        while (isRunning()) {
            if (gameTimer.shouldUpdate()) {
                ticks++;
                tick();
                shouldRender = true;
            }

            /* Suspender el Game Loop antes de renderizar, reduce el tiempo del CPU. La desventaja de esto, es que no se
             * aprovecha el pontencial de una maquina rapida, y en los dispositivos de bajos recursos, genera un pequeño
             * lag.
             * https://www.gamedev.net/forums/topic/445787-game-loop---free-cpu/
             * https://gamedev.stackexchange.com/questions/651/what-should-a-main-game-loop-do/656#656
             * https://stackoverflow.com/questions/10740187/game-loop-frame-independent
             * https://gamedev.stackexchange.com/questions/160329/java-game-loop-efficiency
             * https://stackoverflow.com/questions/18283199/java-main-game-loop */

            // Thread.sleep(1);

            /* La interpolacion del renderizado (desacopla la tasa de frames del timestep fijo) ejecuta el juego a
             * una velocidad de frames variable aprovechando la variabilidad del rendimiento de distintos hardwares,
             * pero la fisica (colisiones, IA, etc.) se mantiene constante. */
            if (shouldRender) {
                frames++;
                render();
            }

            // Temporiza la cantidad de ticks y frames cada un segundo
            if (System.currentTimeMillis() - timer >= 1000) {
                System.out.println(ticks + " ticks, " + frames + " fps");
                timer = System.currentTimeMillis();
                ticks = 0;
                frames = 0;
            }

        }

    }

    private void tick() {
    }

    private void render() {
    }

    private synchronized void start() {
        if (running) return;
        running = true;
        thread = new Thread(this, "Game Thread");
        thread.start();
    }

    private synchronized void stop() {
        if (!running) return;
        running = false;
        // FIXME Usar join() desde esta funcion no finaliza el subproceso y por lo tanto la aplicacion queda en ejecucion
		/*try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
    }

    private synchronized boolean isRunning() {
        return running;
    }

    private void sleep() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GameLoop game = new GameLoop();
        game.start();
        game.sleep();
        game.stop();
        // FIXME El subproceso del juego sigue vivo despues de salir del Game Loop
        if (!game.thread.isAlive()) System.out.println(game.thread.getName() + " muerto!");
    }

}
