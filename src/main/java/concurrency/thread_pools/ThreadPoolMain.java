package concurrency.thread_pools;

public class ThreadPoolMain {

	public static void main(String[] args) throws Exception {

		// Crea un grupo de 3 subprocesos con la capidad de ejecutar un maximo de 10 tareas
		ThreadPool threadPool = new ThreadPool(3, 10);

		for (int i = 0; i < 10; i++) {

			int taskNo = i;
			threadPool.execute(() -> {
				String message = Thread.currentThread().getName() + ": Task " + taskNo;
				System.out.println(message);
			});
		}

		// Espere hasta que todas las tareas terminen
		threadPool.waitUntilAllTasksFinished();

		threadPool.stop();
	}

}
