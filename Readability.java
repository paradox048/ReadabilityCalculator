import java.util.Scanner;
import java.io.IOException;
import java.io.File;
public class Readability {

        public static void main(String[] args) {
            //stores the txt file in the String txtFile
            String txtFile = args[0];

            try (Scanner scanner = new Scanner(new File(txtFile))) {
                //takes the paragraph and trims the extra spaces
                String input = scanner.nextLine().trim();

                //splits the paragraph into sentences and words
                String[] split = input.split("[\\. ]+");
                String[] theSentences = input.split("[\\?\\!\\.]+");

                //variables
                int words = split.length;
                int sentences = theSentences.length;
                int characters = amountOfCharacters(input);
                int syllables = getSyllables(input);
                int polysyllables = getPollySyllables(split);
                double ariScore = calculateAriScore(words, characters, sentences);
                double fkScore = calculateFleschScore(words, sentences, syllables);
                double smogIndex = smogIndex(polysyllables, sentences);
                double clIndex = clIndex(characters, sentences, words);

                //asks the user for a specific input
                System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
                String userChoice = scanner.nextLine();

                //prints out the words, sentences, characters, syllables and polysyllables
                System.out.println("Words: " + words);
                System.out.println("Sentences: " + sentences);
                System.out.println("Characters: " + characters);
                System.out.println("Syllables: " + syllables);
                System.out.println("Polysyllables: " + polysyllables);

                //depending on the users choice switch statement will execute commands
                switch(userChoice) {
                    case "ARI":
                        System.out.println("Automated Readability Index: " + ariScore + " (about " + ageGroup(ariScore) + ")");
                        break;
                    case "FK":
                        System.out.println("Flesch–Kincaid readability tests: " + fkScore + " (about " + ageGroup(fkScore) + " year olds)");
                        break;
                    case "SMOG":
                        System.out.println("Simple Measure of Gobbledygook: " + smogIndex + " (about " + ageGroup(smogIndex) + " year olds)");
                        break;
                    case "CL":
                        System.out.println("Coleman–Liau index: " + clIndex + " (about " + ageGroup(clIndex) + ")");
                        break;
                    case "all":
                        System.out.println("Automated Readability Index: " + ariScore + " (about " + ageGroup(ariScore) + ")");
                        System.out.println("Flesch–Kincaid readability tests: " + fkScore + " (about " + ageGroup(fkScore) + " year olds)");
                        System.out.println("Simple Measure of Gobbledygook: " + smogIndex + " (about " + ageGroup(smogIndex) + " year olds)");
                        System.out.println("Coleman–Liau index: " + clIndex + " (about " + ageGroup(clIndex) + ")");
                        System.out.println("This text should be understood by " + ageGroup(ariScore));
                        break;
                }
                System.out.println("This text should be understood by " + ageGroup(ariScore));

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    /**
     * returns the amount of syllables of a passed String
     * @param input     A String that the method will count the syllables of
     * @return          returns the amount of syllables in the String
     */
    private static int getSyllables(String input) {
            String newString = input.replaceAll("e\\b", "")
                    .replaceAll("you", "a")
                    .replaceAll("[aeiouy]{2}", "a")
                    .replaceAll(" th "," a ")
                    .replaceAll(",","")
                    .replaceAll(" w "," a ")
                    .replaceAll("[0-9]+", "a")
                    .replaceAll("[^aeiouy]", "");
            return newString.length();
        }

    /**
     * Returns the amount of polysyllables in a String array
     * @param split     A String array containing the words of the text
     * @return          returns the amount of polysyllables in the array
     */
    private static int getPollySyllables(String[] split) {
            int poly = 0;
            for (int i = 0; i < split.length; i++) {
                String currentWord = split[i];

                if (getSyllables(currentWord) > 2) {
                    poly++;
                }
            }
            return poly;
        }

    /**
     *
     * @param input     the String that is passed that the characters will be counted from
     * @return          returns the amount of characters in the passed String
     */
    public static int amountOfCharacters(String input) {
            int characters = 0;
            //calculates the characters in the input
            for (int i = 0; i < input.length(); i++) {
                if (input.charAt(i) != ' ') {
                    characters++;
                }
            }
            return characters;
        }

    /**
     * calculates the Flesch - Score
     * @param totalWords        the total amount of words in the text
     * @param totalSentences    the total amount of sentences in the text
     * @param totalSyllables    the total amount of syllables in the text
     * @return                  returns the Flesch Score
     */
        private static double calculateFleschScore(int totalWords, int totalSentences, int totalSyllables) {
            return (0.39 * totalWords / totalSentences) + (11.8 * totalSyllables / totalWords - 15.59);
        }

    /**
     * calculates the smog index
     * @param polysyllables     the amount of polysyllables in the text
     * @param sentences         the amount of sentences in the text
     * @return                  returns the index
     */
        private static double smogIndex(int polysyllables, int sentences) {
            return 1.0430 * Math.sqrt(polysyllables * (30d / sentences)) + 3.1291;
        }

    /**
     * calculates the Automatic Readability Index
     * @param words         total words in the text
     * @param characters    total characters in the text
     * @param sentences     total sentences in the text
     * @return              returns the ARI score
     */
        public static double calculateAriScore(int words, int characters, int sentences) {
            return 4.71 * (characters  * 1d / words) + 0.5 * (words * 1d / sentences) - 21.43;
        }

    /**
     * calculates the Coleman–Liau index
     * @param characters        total characters in the text
     * @param sentences         total sentences in the text
     * @param words             total words in the text
     * @return                  returns the Coleman–Liau index score
     */
        public static double clIndex(int characters, int sentences, int words) {
            return 0.0588 * ((characters / words) * 100d) - 0.296 * ((sentences / words) * 100d) - 15.8;
        }

    /**
     * based on a score the method will return an approximate age
     * @param score     the index of a test
     * @return          returns a String with the approx. age group that are able to read the text
     */
    public static String ageGroup(double score) {
            double rounded = Math.ceil(score);
            int integerScore = (int)rounded;
            switch(integerScore) {
                case 1:
                    return "6 year olds";

                case 2:
                    return "7 year olds";

                case 3:
                    return "9 year olds";

                case 4:
                    return "10 year olds";

                case 5:
                    return "11 year olds";

                case 6:
                    return "12 year olds";

                case 7:
                    return "13 year olds";

                case 8:
                    return "14 year olds";

                case 9:
                    return "15 year olds";

                case 10:
                    return "16 year olds";

                case 11:
                    return "17 year olds";

                case 12:
                    return "18 year olds";

                case 13:
                    return "24 year olds";

                default:
                    return "24+ year olds";

            }
        }
    }


