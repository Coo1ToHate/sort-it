import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MergeSort {
    public static void main(String[] args) {
//        args = new String[]{"-a", "-i", "out.txt", "1.txt", "2.txt", "3.txt"};

        boolean isAsc = true;
        boolean isInt = false;
        boolean isOut = true;
        String outputFile = null;
        List<String> inputFiles = new ArrayList<>();
        List<String> allElements = new ArrayList<>();

        if (args.length < 3) {
            System.out.println("Неправильное количество аргументов!");
            return;
        }

        for (String arg : args) {
            if (arg.equals("-a") || arg.equals("-d")) {
                isAsc = arg.equals("-a");
                continue;
            }
            if (arg.equals("-s") || arg.equals("-i")) {
                isInt = arg.equals("-i");
                continue;
            }
            if (isOut) {
                isOut = false;
                outputFile = arg;
            } else {
                inputFiles.add(arg);
            }
        }

        if (inputFiles.isEmpty()) {
            System.out.println("Нет входных файлов.");
            return;
        }

        System.out.println("Сортировка: " + (isAsc ? "по возрастанию" : "по убыванию"));
        System.out.println("Тип данных: " + (isInt ? "цифры" : "строки"));
        System.out.println("Имя выходного файла: " + outputFile);
        StringBuilder sb = new StringBuilder();
        sb.append("Входные файлы: ").append(System.lineSeparator());
        inputFiles.forEach(i -> sb.append("\t").append(i).append(System.lineSeparator()));
        System.out.println(sb);

        for (String inputFile : inputFiles) {
            List<String> elements = new ArrayList<>();
            try {
                elements = readLines(inputFile);
            } catch (IOException e) {
                System.out.println("Файл " + inputFile + " не найден!");
            }
            allElements.addAll(elements);
        }

        if (isInt) {
            try {
                List<Integer> intElements = new ArrayList<>();
                for (String element : allElements) {
                    intElements.add(Integer.parseInt(element));
                }
                mergeSort(intElements, isAsc);
                for (int i = 0; i < allElements.size(); i++) {
                    allElements.set(i, String.valueOf(intElements.get(i)));
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Невозможно преобразовать в целое число: " + e.getMessage());
                return;
            }
        } else {
            mergeSort(allElements, isAsc);
        }

        try {
            writeLines(allElements, outputFile);
        } catch (IOException e) {
            System.out.println("Ошибка записи: " + e.getMessage());
        }
    }

    private static List<String> readLines(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    private static <T extends Comparable<T>> void mergeSort(List<T> elements, boolean isAsc) {
        if (elements.size() < 2) {
            return;
        }

        int mid = elements.size() / 2;
        List<T> left = new ArrayList<>(elements.subList(0, mid));
        List<T> right = new ArrayList<>(elements.subList(mid, elements.size()));

        mergeSort(left, isAsc);
        mergeSort(right, isAsc);

        merge(elements, left, right, isAsc);
    }

    private static <T extends Comparable<T>> void merge(List<T> elements, List<T> left, List<T> right, boolean isAsc) {
        int i = 0, j = 0, k = 0;

        while (i < left.size() && j < right.size()) {
            T leftValue = left.get(i);
            T rightValue = right.get(j);

            if ((isAsc && leftValue.compareTo(rightValue) <= 0) || (!isAsc && leftValue.compareTo(rightValue) >= 0)) {
                elements.set(k, leftValue);
                i++;
            } else {
                elements.set(k, rightValue);
                j++;
            }
            k++;
        }

        while (i < left.size()) {
            elements.set(k, left.get(i));
            i++;
            k++;
        }

        while (j < right.size()) {
            elements.set(k, right.get(j));
            j++;
            k++;
        }
    }

    private static void writeLines(List<String> lines, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
