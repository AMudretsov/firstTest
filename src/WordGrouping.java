import java.util.*;

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

    /* результирующая структура отсортированных по первой букве массивов слов */
    private TreeMap<Character,List<String>> container=new TreeMap<>();

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
     * Процедура разбора строки
     */
    public void parsing() {
        // разбиваем исходную строку на слова
        String [] words = this.inputString.split(" ");
        // размер для заказа внутренних массивов
        int count=words.length;
        System.out.println("detect " + count + " words");

        // основной цикл перебора слов
        for(String word : words){
            List list;
            Character ch = word.charAt(0);

            if(!this.container.containsKey(ch)){
                // буква встретилась первый раз, заказываем для нее массив
                list=new ArrayList(count);
                this.container.put(ch,list);
            } else {
                // очередное слова с буквой 'ch'
                list=this.container.get(ch);
                // уменьшаем размер следующего массива
                count--;
            }
            list.add(word);
        }

        // сортируем полученные массивы
        for(List<String> list :this.container.values()) {
            Collections.sort(list, new Comparator<String>() {
                public int compare(String s1, String s2) {
                    return s1.length() == s2.length() ? s1.compareTo(s2) : s2.length() - s1.length();
                }
            });
        }
    }

    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder(1024);
        sb.append('[');

        int idx=0;  // для отсечения последней ','
        // перебор 'Map' через 'Set'
        Set<Map.Entry <Character,List<String>>>entrySet=this.container.entrySet();
        for(Map.Entry <Character,List<String>> entry: entrySet) {
            //получить ключ
            Character ch = entry.getKey();
            //получить значение
            List <String> list = entry.getValue();
            sb.append(ch).append('=').append('[');

            // можно отсечь последнюю ',' если перебор через 'Iterator'
            Iterator iter = list.iterator();
            for (;;) {
                sb.append(iter.next());
                if (!iter.hasNext()) break;
                sb.append(',');
            }
            sb.append(']');
            if (++idx!=entrySet.size())
                sb.append(',');
        }

        sb.append(']');
        return sb.toString();
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
        wg.parsing();
        System.out.println(wg);
    }
}
