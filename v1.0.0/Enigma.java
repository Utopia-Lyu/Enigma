/************************************************************************
 * Program version : Enigma 1.0.0                                       *
 * Last Update: 2017-11-10                                              *
 * Author: Hongxiao Lyu                                                 *
 *                                                                      *
 * This is a program imitating the Enigma encrypting algorithm in the   *
 * second world war. It could both encode and decode refering to a      *
 * given rule sets.                                                     *
 *                                                                      *
 * Files needed:                                                        *
 *    code.txt    // The original char set.                             *
 *    key.txt     // The corresponding cypher char set.                 *
 *    rotor.txt   // The original setting of the rotors.                *
 *                                                                      *
 ************************************************************************/

import java.util.*;
import java.io.*;

public class Enigma {

   public static int[] rotor;
   public static char[] code;
   public static char[] key;
   
   public static int rotors;
   
   public static void main(String[] args) throws FileNotFoundException {
      initialization();
   
      Scanner console = new Scanner(System.in);
      System.out.print("Encode --> 'e' or docode --> 'd'? ");
      boolean pass = false;
      while (!pass) {
         String option = console.nextLine();
         char optionChar = option.charAt(0);
         if (optionChar == 'e' || optionChar == 'E') {
            pass = true;
            startEncode();
         } else if (optionChar == 'd' || optionChar == 'D') {
            pass = true;
            startDecode();
         } else {
            System.out.println("Invalid input! please enter again.");
            System.out.print("Encode --> 'e' or docode --> 'd'? ");
         }
      }
   }
   
   public static void initialization() throws FileNotFoundException {
      Scanner input1 = new Scanner(new File("code1.txt"));
      String code0 = input1.nextLine();
      code = new char[code0.length()];
      Scanner input2 = new Scanner(new File("key.txt"));
      String key0 = input2.nextLine();
      key = new char[key0.length()];
      if (key.length != code.length) {
         System.out.println("Code / key file invalid!");
         System.exit(1);
      }
      for (int i = 0; i < code0.length(); i++) {
         code[i] = code0.charAt(i);
         key[i] = key0.charAt(i);
      }
      
      Scanner input3 = new Scanner(new File("rotor.txt"));
      List<Integer> rotorIni = new ArrayList<Integer>();
      if (!input3.hasNextInt()) {
         System.out.println("Rotor file invalid!");
         System.exit(1);
      }
      rotors = input3.nextInt();
      while (input3.hasNextInt()) {
         rotorIni.add(input3.nextInt());
      }
      if (rotorIni.size() != rotors) {
         System.out.println("Rotor file invalid!");
         System.exit(1);
      }
      rotor = new int[rotors];
      for (int i = 0; i < rotorIni.size(); i++) {
         rotor[i] = rotorIni.get(i);
      }
      updateRotor();
   }
   
   public static void startEncode() throws FileNotFoundException {
      Scanner toCode = new Scanner(new File("bigoutput.txt"));
      PrintStream coded = new PrintStream(new File("codedText.txt"));
      process('e', toCode, coded);
   }
   
   public static void startDecode() throws FileNotFoundException {
      Scanner toDecode = new Scanner(new File("codedText.txt"));
      PrintStream decoded = new PrintStream(new File("decodedText.txt"));
      process('d', toDecode, decoded);
   }
   
   public static void process(char ed, Scanner toDo, PrintStream after) {
      while (toDo.hasNextLine()) {
         String line = toDo.nextLine();
         for (int i = 0; i < line.length(); i++) {
            char curr = line.charAt(i);
            if (ed == 'e') {
               for (int j = 0; j < rotors; j++) {
                  curr = encode(curr, rotor[j]);
               }
            } else {
               for (int j = 0; j < rotors; j++) {
                  curr = decode(curr, rotor[rotor.length - j - 1]);
               }
            }
            rotor[rotor.length - 1]++;
            updateRotor();
            after.print(curr);
         }
         after.println();
      }
   }
   
   public static char encode(char curr, int rotor) {
      int find = 0;
      for (int i = 0; i < key.length; i++) {
         if (curr == key[i]) {
            find = i;
            break;
         }
      }
      curr = code[(rotor + find) % code.length];
      return curr;
   }
   
   public static char decode(char curr, int rotor) {
      int location = 0;
      for (int i = 0; i < code.length; i++) {
         if (curr == code[i]) {
            location = i;
            break;
         }
      }
      curr = key[(location - rotor + key.length) % key.length];
      return curr;
   }
   
   public static void updateRotor() {
      for (int i = 0; i < rotor.length - 1; i++) {
         if(rotor[rotor.length - i - 1] >= code.length) {
            rotor[rotor.length - i - 1] %= code.length;
            rotor[rotor.length - i - 2]++;
         }
      }
      if(rotor[0] >= code.length) {
         rotor[0] %= code.length;
      }
   }

}