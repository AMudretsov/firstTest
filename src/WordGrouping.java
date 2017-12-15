import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Created by AMudretsov on 14.12.2017.
 *
 * Класс разбора входной строки из слов, на отсортированную структуру по первой букве в слове.
 * Структура содержит:
 *  -   первую букву в слове
 *  -   массив слов, начинающихся с этой буквы, у которого слова внутри группы
 *      сортированы по убыванию (по количеству символов);
 *      если число символов равное, то сортировать в алфавитном порядке.
 */

public class WordGrouping {

    /* Исходная строка */
    public final String inputString;

    /**
     * Конструктор класса
     * @param inputString - строка для разбора
     */
    public WordGrouping(String inputString) {
        if (inputString.trim().length()==0)
            throw new IllegalArgumentException("an empty string is passed to the input");

        this.inputString = inputString.trim();
    }

    /**
     * Процедура разбора строки в Java 8
     */
    public List parsing8() {
        return  Arrays.stream(this.inputString.split(" "))
            // производим группировку по первой букве
            .collect(Collectors.groupingBy(s -> s.charAt(0), TreeMap<Character, List<String>>::new, Collectors.mapping(Function.identity(), Collectors.toList())))
                    // убираем группы из одного слова
            .entrySet().stream().filter(entry -> entry.getValue().size() > 1)
                    // сортируем по убывания числа букв в слове, а затем по алфавиту
            .map(entry -> {
                entry.setValue(entry.getValue()
                     .stream()
                     .sorted((s1, s2) -> s1.length() == s2.length() ? s1.compareTo(s2) : s2.length() - s1.length())
                     .collect(Collectors.toList())
                );
                return entry;
            })
            .collect(Collectors.toList());
    }

    /**
     * Процедура разбора строки в Java 7
     */
    public List parsing7() {
        // разбиваем исходную строку на слова
        String [] words = this.inputString.split(" ");
        // размер для заказа внутренних массивов
        int count=words.length;

        // результирующая структура отсортированных по первой букве слов массивов
        Map<Character,List<String>> map=new TreeMap<>();
        // производим группировку по первой букве
        for(String word : words){
            List list;
            Character ch = word.charAt(0);

            if(!map.containsKey(ch)){
                // буква встретилась первый раз, заказываем для нее массив
                list=new ArrayList(count);
                map.put(ch,list);
            } else {
                // очередное слова с буквой 'ch'
                list=map.get(ch);
                // уменьшаем размер следующего массива
                count--;
            }
            list.add(word);
        }

        List list=new ArrayList<>(map.size());
        for(Map.Entry<Character,List<String>> entry : map.entrySet()) {
            if (entry.getValue().size() == 1) continue;
            Collections.sort(entry.getValue(), new Comparator<String>() {
                public int compare(String s1, String s2) {
                    return s1.length() == s2.length() ? s1.compareTo(s2) : s2.length() - s1.length();
                }
            });
            list.add(entry);
        }
        return list;
    }

    public static void main(String [] a) {
        /* проверка корректности входных параметров */
        if (a.length == 0){
            System.out.println("no parameter, requires an input string");
            System.out.println("example:\n\"no parameter requires an input string\"");
            return;
        } else
        if (a.length > 1){
            System.out.println("only one parameter is needed, the input string");
            System.out.println("example:\n\"only one parameter is needed the input string\"");
            return;
        }

        System.out.println("\ninput string for parsing:");
        System.out.println(a[0]);

        WordGrouping wg=new WordGrouping(a[0]);
        long timeStream=System.currentTimeMillis();
        List listStream=wg.parsing8();
        timeStream=System.currentTimeMillis()-timeStream;
        System.out.println(timeStream+" : "+listStream);

        long timeforEach=System.currentTimeMillis();
        List listforEach=wg.parsing7();
        timeforEach=System.currentTimeMillis()-timeforEach;
        System.out.println(timeforEach+" : "+listforEach);

        System.out.println((timeforEach < timeStream)+" : " + wg.parsing8().equals(wg.parsing7()));
    }
}
