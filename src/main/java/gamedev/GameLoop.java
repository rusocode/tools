package gamedev;

/**
 * <h1>Game Loop</h1>
 * El Game Loop es el control de flujo general para todo el programa del juego. Es un bucle porque el juego sigue
 * realizando una serie de acciones una y otra vez hasta que el usuario sale. Las partes basicas del Game Loop son las
 * siguientes y van por orden: la entrada del usuario (que se procesa sin bloquear), la simulacion de la fisica y el
 * renderizado.
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
 * <br><br>
 * <h2>La primera solucion (Tomando una pequeña siesta)</h2>
 * La primera variacion que veremos agrega una solucion simple. Digamos que quieres que tu juego se ejecute a 60 FPS.
 * Eso le da alrededor de 16 milisegundos por frame. Siempre que pueda realizar todo el procesamiento y renderizado de
 * su juego de manera confiable en menos de ese tiempo, puede ejecutar a una velocidad de fotogramas constante. Todo lo
 * que hace es procesar el frame y luego <i>esperar</i> hasta que sea el momento para el siguiente. Esta solucion se
 * aplica utilizando el metodo {@code sleep()}. Ver la imagen <i>"Game Loop simple usando sleep().png"</i>.
 * <p>
 * Aqui se asegura de que usando {@code sleep()}, el juego no se ejecute demasiado rapido si procesa un frame
 * rapidamente. Aunque esto no ayuda si su juego se ejecuta demasiado lento. Si se tarda mas de 16 ms en actualizar y
 * renderizar el frame, el tiempo de suspension se vuelve negativo.
 * <p>
 * Puede solucionar esto haciendo menos trabajo en cada frame: reduzca los graficos y simplifique la IA. Pero eso
 * afecta la calidad del juego para todos los usuarios, incluso los de maquinas rapidas.
 * <br><br>
 * <h2>La segunda solucion (Un pequeño paso, un paso gigante)</h2>
 * Entonces... para que el juego se ejecute a una velocidad constante sin reducir los graficos y simplificar la IA, se
 * aplica el <b>{@link Delta Delta Time}</b>.
 * <p>
 * La logica que usa el Delta Time es elegir un <a href="https://www.gamedev.net/forums/topic/673798-what-is-a-timestep/">timestep</a>
 * para avanzar en funcion de cuanto tiempo <i>real</i> paso desde el ultimo frame. Cuanto mas tiempo toma el frame, mas
 * pasos toma el juego. Siempre se mantiene en tiempo real porque se necesitaran pasos cada vez mas grandes para llegar
 * alli. Llaman a esto timestep <i>variable</i> o <i>fijo</i>.
 * <p>
 * En cada frame, determinamos cuanto tiempo <i>real</i> paso desde la ultima actualizacion del juego ({@code elapsed}).
 * Cuando actualizamos el estado del juego, lo pasamos. El motor es entonces responsable de hacer avanzar el mundo del
 * juego por esa cantidad de tiempo.
 * <p>
 * Digamos que tienes una bala disparando a traves de la pantalla. Con un timestep fijo, en cada frame, la movera de
 * acuerdo con su velocidad. Con un timestep variable, <i>escala esa velocidad por el tiempo transcurrido</i>. A medida
 * que aumenta el timestep, la bala se mueve mas lejos en cada frame. Esa bala atravesara la pantalla en la misma
 * cantidad de tiempo real, ya sean en veinte pequeños pasos rapidos o en cuatro grandes pasos lentos. Esto se ve muy
 * bien:
 * <ul>
 * <li>El juego se juega a un ritmo constante en diferentes hardware.
 * <li>Los jugadores con maquinas mas rapidas son recompensados con un juego mas fluido.
 * </ul>
 * <p>
 * Pero, por desgracia, hay un grave problema al acecho: hemos hecho que el juego sea inestable y no determinista. He
 * aqui un ejemplo de la trampa que nos hemos tendido a nosotros mismos:
 * <p>
 * Digamos que tenemos un juego en red para dos jugadores y Ruso tiene una maquina de juego increible mientras que
 * Rulo esta usando la PC antigua de su abuela. Esa bala antes mencionada esta volando a traves de sus dos pantallas.
 * En la maquina de Ruso, el juego se ejecuta super rapido, por lo que cada timestep es pequeño. Metemos como 50
 * fotogramas en el segundo que tarda la bala en atravesar la pantalla. La maquina del pobre Rulo solo puede caber en
 * unos cinco frames.
 *
 * <p>Esto significa que en la maquina de Ruso, el motor de fisica actualiza la posicion de la bala 50 veces, pero la de
 * Rulo solo lo hace cinco veces. La mayoria de los juegos usan numeros de punto flotante y estan sujetos a <i>errores
 * de redondeo.</i> Cada vez que agrega dos numeros de punto flotante, la respuesta que obtiene puede ser un poco
 * incorrecta. La maquina de Ruso esta haciendo diez veces mas operaciones, por lo que acumulara un error mayor que el
 * de Rulo. La <i>misma</i> bala terminara en <i>diferentes lugares</i> de sus maquinas.
 *
 * <p>Este es solo un problema desagradable que puede causar un timestep variable, pero hay mas. Para funcionar en
 * tiempo real, los motores de fisica del juego son aproximaciones de las leyes reales de la mecanica. Para evitar que
 * esas aproximaciones exploten, se aplica amortiguacion (interpolacion?). Esa amortiguacion se ajusta cuidadosamente a
 * un cierto timestep. Varie eso, y la fisica se vuelve inestable.
 *
 * <p>Esta inestabilidad es tan mala que este ejemplo solo esta aqui como una advertencia y para llevarnos a algo mejor...
 *
 * <br><br>
 *
 * <h2>Interpolando la fisica (Jugar a ponerse al dia)</h2>
 * Una parte del motor que normalmente <i>no se</i> ve afectada por un timestep variable es el renderizado. Dado que el
 * motor de renderizado captura un instante en el tiempo, no importa cuanto tiempo haya avanzado desde el ultimo.
 * Representa las cosas dondequiera que esten en ese momento.
 *
 * <p>Podemos usar este hecho a nuestro favor. <i>Actualizaremos</i> el juego usando un timestep fijo porque eso hace
 * que todo sea mas simple y mas estable para la fisica y la IA. Pero permitiremos flexibilidad cuando <i>rendericemos</i>
 * para liberar algo de tiempo de procesador.
 *
 * <p>Es asi: ha transcurrido una cierta cantidad de tiempo real desde el ultimo turno del ciclo del juego. Esta es la
 * cantidad de tiempo de juego que necesitamos simular para que el "ahora" del juego se ponga al dia con el del jugador.
 * Hacemos eso usando una <i>serie</i> de timesteps <i>fijos</i>. Ver {@link GameLoop#run()}.
 *
 * <p>Hay algunas partes aqui. Al comienzo de cada frame, actualizamos {@code lag} en funcion de cuanto tiempo real paso.
 * Esto mide que tan atrasado esta el reloj del juego en comparacion con el mundo real. Luego tenemos un ciclo interno
 * para actualizar el juego, un paso fijo a la vez, hasta que se pone al dia. Una vez que estamos al dia, renderizamos y
 * comenzamos de nuevo. Ver la imagen <i>"Game Loop aplicando tiempo delta y interpolacion de fisica.png".</i>
 *
 * <p>Tenga en cuenta que el paso de tiempo aqui ya no es la velocidad de fotogramas <i>visible</i>. El tiempo delta es
 * solo la <a href="https://es.theastrologypage.com/granularity"><i>granularidad</i></a> que usamos para actualizar el
 * juego. Cuanto mas corto sea este paso, mas tiempo de procesamiento se necesita para ponerse al dia en tiempo real.
 * Cuanto mas largo es, mas entrecortado es el juego. Lo ideal es que sea bastante corto, a menudo mas rapido que 60 FPS,
 * para que el juego simule con alta fidelidad en maquinas rapidas.
 *
 * <p>Pero tenga cuidado de no hacerlo <i>demasiado</i> corto. Debe asegurarse de que el timestep sea mayor que el
 * tiempo que lleva procesar una actualizacion, incluso en el hardware mas lento. De lo contrario, su juego simplemente
 * no puede ponerse al dia.
 *
 * <p>Afortunadamente, nos hemos comprado un respiro aqui. El truco es que <i>hemos eliminado el renderizado del ciclo
 * de actualizacion</i>. Eso libera un monton de tiempo de CPU. El resultado final es que el juego <i>simula</i> a una
 * velocidad constante utilizando timesteps fijos y seguros en una variedad de hardware. Es solo que la <i>ventana
 * visible</i> del jugador en el juego se vuelve mas entrecortada en una maquina mas lenta.
 *
 * <br><br>
 *
 * <h3>Extracciones importantes de buenos articulos</h3>
 * Extraccion de <a href="https://www.reddit.com/r/gamedev/comments/8sci48/should_i_be_using_a_fixed_timestep_or_delta_time/">¿Debo usar un timestep fijo o un tiempo delta?</a>
 * <br>
 * La <i>interpolacion</i> hace que el juego se ejecute a una velocidad de fotogramas variable, pero sus sistemas
 * fisicos y de red se actualizan 60 veces por segundo. Interpolaria entre valores conocidos (hace dos ticks y hace un
 * tick) para que el resto de su juego pueda ejecutarse lo mas rapido posible y la velocidad de fotogramas se desacople
 * del timestep fijo.
 *
 * <p>Extracion de <a href="https://www.reddit.com/r/gamedev/comments/22k6pl/fixed_time_step_vs_variable_time_step/">Timestep fijo vs timestep variable</a>
 * <br>
 * La mayor ventaja del paso fijo es la consistencia. Puede grabar solo la entrada del jugador en cada cuadro,
 * reproducirlo y ver una recreacion perfecta de todo lo que sucedio. Tambien es mas facil de desarrollar de muchas
 * maneras, ya que no tiene que preocuparse por multiplicar dt (delta time) por todo."
 *
 * <p>Ademas, el paso variable significa que el juego se mueve a una velocidad constante independientemente de la
 * velocidad de fotogramas, lo que es excelente para admitir una amplia variedad de hardware."
 *
 * <p>La mayor desventaja del paso variable es que tiende a explotar a velocidades de cuadro realmente bajas. Una
 * vez que dt se vuelve excesivamente grande, los objetos comienzan a moverse grandes distancias entre cuadros, lo que,
 * a menos que sea MUY cuidadoso, generalmente resulta en colisiones perdidas y bucles de
 * retroalimentacion/sobrecorreccion similares a resortes que arruinan el juego espectacularmente. En muchos casos, es
 * ventajoso limitar el dt a un valor maximo, volviendo efectivamente al paso fijo si la velocidad de fotogramas es lo
 * suficientemente mala.
 *
 * <p>Extraccion de <a href="https://gamedev.stackexchange.com/questions/132831/what-is-the-point-of-update-independent-rendering-in-a-game-loop">¿Cual es el punto de actualizar independientemente del renderizado?</a>
 * <br>
 * <b>2. Entonces, ¿Por que no simplemente bloquear la velocidad de fotogramas (por ejemplo, usando VSync) y seguir
 * ejecutando las actualizaciones de estado del juego y la renderizacion al mismo tiempo?</b>
 * <br>
 * El desacoplamiento de las tasas de actualizacion y renderizacion brinda mas flexibilidad para lidiar con la
 * variabilidad del rendimiento.
 *
 * <p><b>3. ¿La actualizacion en un intervalo de tiempo fijo no tiene los mismos problemas que (2)?</b>
 * <br>
 * a) La velocidad de actualizacion puede ser mas rapida que la velocidad de fotogramas renderizada. Ahora, el jugador
 * que renderiza a 120 FPS puede obtener 2 actualizaciones por cuadro, mientras que el jugador con renderizado de
 * hardware de menor especificacion a 30 FPS obtiene 8 actualizaciones por cuadro, y ambos juegos se ejecutan a la misma
 * velocidad de juego por segundo en tiempo real. El mejor hardware hace que parezca mas fluido, pero no altera
 * radicalmente el funcionamiento del juego.
 *
 * <p>Extracion de <a href="https://gamedev.stackexchange.com/questions/1589/when-should-i-use-a-fixed-or-variable-time-step">¿Cuando debo usar un timestep fijo o variable?</a></h2>
 * <br>
 * <b><i>Timestep variable</i></b>
 * <br>
 * Las actualizaciones de fisica reciben un argumento de "tiempo transcurrido desde la ultima actualizacion" y, por
 * lo tanto, dependen de la velocidad de fotogramas. Esto puede significar hacer calculos como {@code position += distancePerSecond * timeElapsed}.
 *
 * <p><i>Pros</i>: suave, mas fácil de codificar
 * <br>
 * <i>Contras</i>: no determinista, impredecible en pasos muy pequeños o grandes
 *
 * <p><b><i>Timestep fijo</i></b>
 * <br>
 * Es posible que las actualizaciones ni siquiera acepten un "tiempo transcurrido", ya que asumen que cada actualizacion
 * es por un periodo de tiempo fijo. Los calculos se pueden realizar como {@code position += distancePerUpdate}. El
 * ejemplo incluye una interpolacion durante el renderizado.
 *
 * <p><i>Pros</i>: predecible, determinista (¿mas facil de sincronizar con la red?), codigo de calculo mas claro
 * <br>
 * <i>Contras</i>: no sincronizado para monitorear v-sync (causa graficos nerviosos a menos que interpole), velocidad
 * de cuadro maxima limitada (a menos que interpole), dificil de trabajar dentro de marcos que asumen timesteps
 * variables (como Pyglet o Flixel).
 *
 * <p><i>Despues de analizar los pros y contras de cada timestep, quedan dos cuestiones importantes...</i>
 * <ul>
 * <li>¿Deberia vincularse la velocidad de paso de la fisica a la velocidad de fotogramas?
 * <li>¿Debe la fisica ser escalonada con deltas constantes?
 * </ul>
 *
 * <p>En <i>Arregla tu timestep!</i> de Glen Fiedler, dice
 * "Liberar la fisica". Eso significa que su tasa de actualizacion de fisica <b>no</b> debe estar vinculada a su tasa de
 * fotogramas.
 *
 * <p>"Entonces, lo que queremos es lo mejor de ambos mundos: un valor de tiempo delta fijo para la simulacion mas la
 * capacidad de renderizar a diferentes velocidades de cuadro. Estas dos cosas parecen completamente opuestas, y lo son,
 * a menos que podamos encontrar una manera de desacoplar la simulacion y la velocidad de fotogramas de renderizado."
 *
 * <p>"Aqui esta como hacerlo. Haga avanzar la simulacion fisica en pasos de tiempo de dt fijos y, al mismo tiempo,
 * asegurese de que se mantiene al dia con los valores del temporizador que provienen del renderizador para que la
 * simulacion avance a la velocidad correcta. Por ejemplo, si la velocidad de fotogramas de la pantalla es de 50 FPS y
 * la simulacion se ejecuta a 100 FPS, debemos realizar dos pasos fisicos en cada actualizacion de la pantalla. Facil."
 *
 * <p>En las recomendaciones de Erin Catto para Box2D, el tambien aboga por esto.
 *
 * <p>"Por lo tanto, no vincule el paso de tiempo a su velocidad de cuadros (a menos que realmente tenga que hacerlo)."
 *
 * <p><i>¿Debe vincularse la frecuencia de pasos de fisica con la velocidad de fotogramas?</i> No.
 * <hr>
 * <p>Los pensamientos de Erin sobre el paso fijo frente al paso variable:
 *
 * <p>"Box2D utiliza un algoritmo computacional llamado integrador. Los integradores simulan las ecuaciones fisicas en
 * puntos de tiempo discretos. ... Tampoco nos gusta que el paso del tiempo cambie mucho. Un paso de tiempo variable
 * produce resultados variables, lo que dificulta la depuracion."
 *
 * <p>Los pensamientos de Glen sobre pasos fijos vs variables:
 *
 * <p><b>Arregla tu timestep o explota</b>
 *
 * <p>"... Si tiene una serie de restricciones de resorte realmente rigidas para los amortiguadores en una simulacion de
 * automovil, entonces pequeños cambios en dt pueden hacer que la simulacion explote. ..."
 *
 * <p><i>¿Debe la fisica ser escalonada con deltas constantes?</i> Si.
 * <hr>
 * <p>
 * Doom dibujara marcos lo más rápido posible. Pero no ejecuta la lógica del juego en cada cuadro que dibuja, solo
 * interpola el estado mundial entre las actualizaciones del juego anteriores y actuales.
 *
 * <p>La mayor parte de la documentacion la tome de <a href="http://gameprogrammingpatterns.com/game-loop.html">Game Loop</a>.
 *
 * <p>Recursos:
 * <a href="https://gafferongames.com/post/fix_your_timestep/">Arregla tu timestep!</a>
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

    /**
     * La idea detras de este codigo es mantener un bucle de juego que actualice la logica del juego a una velocidad
     * constante (60 ticks por segundo en este caso), independientemente de la velocidad de la maquina en la que se este
     * ejecutando el juego. El tiempo no procesado se acumula entre actualizaciones y se utiliza para asegurarse de que
     * el juego mantenga una velocidad constante incluso si el bucle se ejecuta mas rapido o mas lento en diferentes
     * sistemas.
     */
    @Override
    public void run() {

        Delta delta = new Delta();
        int ticks = 0, frames = 0;
        boolean shouldRender = false;
        long timer = System.currentTimeMillis();

        while (isRunning()) {
            /* Interpola la fisica usando el delta. Ademas, la ventaja de comprobar el delta dentro del Game Loop, es
             * que no necesita multiplicar todo lo relacionado con la fisica por el delta. Esto hace que se actualice
             * la fisica independientemente de los FPS. */
            if (delta.checkDelta()) {
                ticks++;
                tick();
                shouldRender = true; // Actualiza primero para tener algo que renderizar en la primera iteracion
            }

            /* Suspender el Game Loop antes de renderizar, reduce el tiempo del CPU. La desventaja de esto, es que no se
             * aprovecha el pontencial de una maquina rapida, y en los dispositivos de bajos recursos, genera un pequeño
             * lag.
             * https://www.gamedev.net/forums/topic/445787-game-loop---free-cpu/
             * https://gamedev.stackexchange.com/questions/651/what-should-a-main-game-loop-do/656#656
             * https://stackoverflow.com/questions/10740187/game-loop-frame-independent
             * https://gamedev.stackexchange.com/questions/160329/java-game-loop-efficiency
             * https://stackoverflow.com/questions/18283199/java-main-game-loop */

			/* try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/

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

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GameLoop game = new GameLoop();
        game.start();
        game.sleep(10 * 1000);
        game.stop();
        // FIXME El subproceso del juego sigue vivo despues de salir del Game Loop
        if (!game.thread.isAlive()) System.out.println(game.thread.getName() + " muerto!");
    }

}
