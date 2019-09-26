import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Human john = new Human("Johnny", "Depp", 51);
        save(john, "fields.txt"); //сохраняем аннотированные файлы
        List<Object> stringList = read(new File("fields.txt")); //вычитка аннотированных файлов
        stringList.forEach(System.out::println);
    }

    public static void save(Object obj, String path) {
        Class humanClass = obj.getClass();
        Field[] fields = humanClass.getDeclaredFields();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(path)))) {
            for (Field field : fields) {
                if (field.isAnnotationPresent(Save.class)) {
                    field.setAccessible(true);
                    oos.writeObject(field.get(obj));
                }
            }
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static List<Object> read(File file) {
        List<Object> objects = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj;
            while ((obj = ois.readObject()) != null) {
                objects.add(obj);
            }
        } catch (IOException | ClassNotFoundException e) {
            return objects;
        }
        return objects;
    }
}
