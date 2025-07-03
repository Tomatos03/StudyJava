/**
 * @Description: 自定义类加载器，从指定 JAR 文件中加载 demo 包下的类
 * @Author: Tomatos
 * @Date: 2025/6/21 20:53
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class MyClassLoader extends ClassLoader {
    // JAR 文件路径
    private final String jarFile;

    /**
     * 构造方法，指定 JAR 文件路径
     *
     * @param jarFile JAR 文件路径
     */
    public MyClassLoader(String jarFile) {
        this.jarFile = jarFile;
    }

    /**
     * 重写 loadClass 方法，实现自定义加载逻辑
     *
     * @param name    类的全限定名
     * @param resolve 是否解析类
     * @return 加载的类对象
     * @throws ClassNotFoundException 找不到类时抛出
     */
    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // 1. 先检查是否已经加载过
        Class<?> c = findLoadedClass(name);
        if (c != null) {
            return c;
        }

        // 2. 只加载 demo 包下的类，其他交给父加载器
        if (name.startsWith("demo")) {
            // 从 JAR 文件中读取类字节码
            byte[] bytes = loadClassData(name);
            if (bytes != null) {
                // 定义类
                c = defineClass(name, bytes, 0, bytes.length);
                if (resolve) {
                    // 解析类
                    resolveClass(c);
                }
                System.out.println("Loaded class: " + name + " using MyClassLoader");
                return c;
            }
        }
        // 3. 其他类还是交给父加载器
        return super.loadClass(name, resolve);
    }

    /**
     * 从 JAR 文件中读取指定类的字节码
     *
     * @param className 类的全限定名
     * @return 类字节码数组，找不到返回 null
     */
    private byte[] loadClassData(String className) {
        // 将类名转换为 JAR 条目名
        String entryName = className.replace('.', '/') + ".class";
        try (JarFile jarFile = new JarFile(this.jarFile)) {
            JarEntry entry = jarFile.getJarEntry(entryName);
            if (entry == null) return null;
            try (InputStream is = jarFile.getInputStream(entry);
                 ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[4096];
                int n;
                // 读取字节流
                while ((n = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, n);
                }
                return baos.toByteArray();
            }
        } catch (IOException e) {
            return null;
        }
    }
}
