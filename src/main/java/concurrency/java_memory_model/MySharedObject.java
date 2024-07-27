package concurrency.java_memory_model;

public class MySharedObject {

    // Variable de clase estatica que es compartida por los dos subprocesos que usan dos referencias que apuntan a esta variable
    public static final MySharedObject sharedInstance = new MySharedObject();

    public Integer object2 = 22;
    public Integer object4 = 44;

    // Como son variables miembro (de clase), se almacenan en el heap junto con el objeto
    public long member1 = 12345;
    public long member2 = 67890;

}
