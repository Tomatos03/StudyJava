import demo.Hello;

/**
 * @Description: TODO
 * @Author: Tomatos
 * @Date: 2025/6/21 20:52
 */
public class Application {
    public static void main(String[] args) throws Exception {
        // 加载第一个 JAR 包里的 Hello
        MyClassLoader loader1 = new MyClassLoader("break-parent-delegation/lib/hello_1.0.jar");
        Class<?> helloClass1 = loader1.loadClass(Hello.class.getName());
        Object hello1 = helloClass1.getDeclaredConstructor().newInstance();
        System.out.println("loader1: " + helloClass1.getClassLoader());
        helloClass1.getMethod("say").invoke(hello1);

        // 加载第二个 JAR 包里的 Hello
        MyClassLoader loader2 = new MyClassLoader("break-parent-delegation/lib/hello_2.0.jar");
        Class<?> helloClass2 = loader2.loadClass(Hello.class.getName());
        Object hello2 = helloClass2.getDeclaredConstructor().newInstance();
        System.out.println("loader2: " + helloClass2.getClassLoader());
        helloClass2.getMethod("say").invoke(hello2);
    }
}
