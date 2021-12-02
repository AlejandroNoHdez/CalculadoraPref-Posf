//Bryan Alejandro Noh Hernández - 6578 - ISC - LENGUAJES Y AUTOMÁTAS II

import java.util.Scanner;
import java.util.Stack;

public class calculadora {

      static Scanner entrada = new Scanner(System.in);

      public static void main(String[] args) {
            String expresion;
            System.out.println("Ingrese la expresión infija:");
            expresion = entrada.next();
            int resultado = calcularPrefijo(expresion);
            int resultado2 = calcularPosfijo(expresion);

            System.out.print("\nEl resultado de la expresión en prefijo es: " + resultado);
            System.out.print("\nEl resultado de la expresión posfijo es: " + resultado2);

      }

      public static int calcularPrefijo(String expresioninf) {

            // Primero convierte la expresión infija en la expresión prefijo
            String prefijaExpresion = toPrefijo(expresioninf);
            System.out.print("\nExpresión prefija: " + prefijaExpresion);

            // Para escanear la expresión de derecha a izquierda, primero invierta el orden
            // de las cadenas
            prefijaExpresion = reverseString(prefijaExpresion);

            char[] chars = prefijaExpresion.toCharArray();

            Stack<Integer> resultValues = new Stack<>();

            for (char theChar : chars) {
                  // Cuando encuentre un número, empuje el número en la pila, aquí debe prestar
                  // atención al número de carácter a int, no puede usar directamente
                  // integer.valueOf, se convertirá al código ascii del carácter
                  if (Character.isDigit(theChar))
                        resultValues.push(Integer.valueOf(String.valueOf(theChar)));
                  // Cuando se encuentra un operador, coloca los dos números en la parte superior
                  // de la pila, usa el operador para hacer los cálculos correspondientes en ellos
                  // (el elemento superior de la pila es el siguiente elemento superior) y empuja
                  // el resultado a la pila;
                  else {
                        int a = resultValues.pop();
                        int b = resultValues.pop();
                        resultValues.push(calculate(a, b, theChar));
                  }
            }
            return resultValues.pop();
      }

      /**
       * Expresión de infijo a expresión de prefijo. (1) Inicializar dos pilas: pila
       * de operador S1 y pila S2 para almacenar resultados intermedios; (2) Escanee
       * la expresión infija de derecha a izquierda; (3) Cuando encuentre un operando,
       * presiónelo en S2; (4) Cuando se encuentre con un operador, compare su
       * prioridad con el operador de la parte superior de la pila S1: (4-1) Si S1
       * está vacío, coloque este operador directamente en la pila; (4-2) De lo
       * contrario, si la prioridad es mayor o igual a la del operador en la parte
       * superior de la pila (mayor en la expresión de sufijo, no hay igualdad) o el
       * operador en la parte superior de la pila es el paréntesis derecho ")", el
       * operador también se presiona Ingrese S1; (4-3) De lo contrario, coloque el
       * operador en la parte superior de la pila S1 y empújelo hacia S2, y vaya a
       * (4-1) nuevamente para comparar con el nuevo operador en la parte superior de
       * la pila en S1; (5) Al encontrar paréntesis: (5-1) Si es el corchete derecho
       * ")", presione S1 directamente; (5-2) Si es el paréntesis izquierdo "(", los
       * operadores en la parte superior de la pila S1 aparecerán a su vez, y se
       * presionará S2 hasta que se encuentre el paréntesis derecho, en este momento
       * el par de paréntesis se descartará; (6) Repita los pasos (2) a (5) hasta el
       * lado izquierdo de la expresión; (7) Muestra los operadores restantes en S1
       * uno por uno y empújelos en S2; (8) Muestra los elementos en S2 a su vez y la
       * salida, y el resultado es la expresión de prefijo correspondiente a la
       * expresión infija. (La expresión de sufijo aquí necesita invertir la salida de
       * la cadena, simplemente envíela directamente aquí)
       *
       * @param expression
       * @return
       */
      private static String toPrefijo(String expression) {

            // (1) Inicializar dos pilas: pila de operadores S1 y pila S2 para almacenar
            // resultados intermedios;
            Stack<Character> ops = new Stack<>();
            Stack<Character> resultValues = new Stack<>();

            // (2) Escanee la expresión infija de derecha a izquierda; aquí es para invertir
            // la cadena y luego recorrer su matriz de caracteres
            expression = reverseString(expression);
            char[] chars = expression.toCharArray();
            for (char theChar : chars) {
                  // (3) Cuando se encuentra el operando, se presiona en S2;
                  if (Character.isDigit(theChar))
                        resultValues.push(theChar);
                  // (4) Al encontrar un operador, compare su prioridad con el operador en la
                  // parte superior de la pila S1:
                  else if (theChar == '+' || theChar == '-' || theChar == '*' || theChar == '/') {
                        dealTheChar(ops, resultValues, theChar);

                  }
                  // (5-1) Si es el corchete derecho ")", presione S1 directamente;
                  else if (theChar == ')')
                        ops.push(theChar);
                  // (5-2) Si es el paréntesis izquierdo "(", los operadores en la parte superior
                  // de la pila S1 aparecerán a su vez, y se presionará S2 hasta que se encuentre
                  // el paréntesis derecho, y este par de paréntesis se descartará;
                  else if (theChar == '(') {
                        char opsChar = ops.pop();
                        while (opsChar != (')')) {
                              resultValues.push(opsChar);
                              opsChar = ops.pop();
                        }
                  }
            }

            // (7) Mostrar los operadores restantes en S1 y enviarlos a S2;
            while (!ops.empty()) {
                  resultValues.push(ops.pop());
            }

            // (8) Muestra los elementos en S2 a su vez y muestra la salida, y el resultado
            // es la expresión de prefijo correspondiente a la expresión infija.
            // (La expresión de sufijo aquí necesita invertir la salida de la cadena,
            // simplemente envíela directamente aquí)
            StringBuilder exp = new StringBuilder();
            while (!resultValues.empty())
                  exp.append(resultValues.pop());

            return exp.toString();
      }

      private static String reverseString(String expression) {
            int n = expression.length();
            char[] chars = expression.toCharArray();
            for (int i = 0; i < n / 2; i++) {
                  char temp = chars[i];
                  chars[i] = chars[n - 1 - i];
                  chars[n - 1 - i] = temp;
            }
            return String.valueOf(chars);
      }

      private static void dealTheChar(Stack<Character> ops, Stack<Character> resultValues, char theChar) {
            // (4-1) Si S1 está vacío, coloque este operador directamente en la pila;
            if (ops.empty()) {
                  ops.push(theChar);
                  return;
            }
            char popTheChar = ops.pop().charValue();
            // (4-1) Si S1 está vacío, o el operador en la parte superior de la pila es el
            // paréntesis de cierre ")", entonces este operador se coloca directamente en la
            // pila;
            // (4-2) De lo contrario, si la prioridad es mayor o igual que el operador en la
            // parte superior de la pila (mayor en la expresión de sufijo, no hay igual) o
            // el operador en la parte superior de la pila es el paréntesis derecho ")", la
            // operación también se realizará El carácter se presiona en S1;
            if (popTheChar == ')' || getPriorityValue(theChar) >= getPriorityValue(popTheChar)) {
                  ops.push(popTheChar);
                  ops.push(theChar);
            }
            // (4-3) De lo contrario, coloque el operador en la parte superior de la pila S1
            // y empújelo hacia S2, y vaya a (4-1) nuevamente para comparar con el nuevo
            // operador en la parte superior de la pila en S1;
            else {
                  resultValues.push(popTheChar);
                  dealTheChar(ops, resultValues, theChar);
            }

      }

      private static int getPriorityValue(char c) {
            if (c == '+' || c == '-')
                  return 0;
            if (c == '*' || c == '/')
                  return 1;
            throw new RuntimeException("operador ilegal");
      }

      private static int calculate(int a, int b, char c) {
            switch (c) {
            case '+':
                  return a + b;
            case '-':
                  return a - b;
            case '*':
                  return a * b;
            case '/':
                  return a / b;
            default:
                  throw new RuntimeException("operador ilegal");
            }
      }

      /**
       * Evaluación informática de expresiones postfijas: Similar a la expresión de
       * prefijo, pero el orden es de izquierda a derecha: Escanee la expresión de
       * izquierda a derecha, cuando encuentre un número, empuje el número en la pila,
       * y cuando encuentre un operador, Coloque los dos números en la parte superior
       * de la pila y use operadores para calcularlos en consecuencia (el siguiente
       * elemento superior op es el elemento superior de la pila), Ponga el resultado
       * en la pila; repita el proceso anterior hasta el extremo derecho de la
       * expresión, y el valor obtenido por la operación final es el resultado de la
       * expresión.
       * 
       * @param expresioninf
       * @return
       */
      public static int calcularPosfijo(String expresioninf) {

            // Primero convierta la expresión infija a la expresión sufijo
            String PostExpresion = toPostfijo(expresioninf);
            System.out.println("\nExpresión posfija: " + PostExpresion);

            char[] chars = PostExpresion.toCharArray();

            Stack<Integer> resultValues = new Stack<>();

            for (char theChar : chars) {
                  // Escanee la expresión de izquierda a derecha, cuando encuentre un número,
                  // empuje el número en la pila
                  if (Character.isDigit(theChar))
                        resultValues.push(Integer.valueOf(String.valueOf(theChar)));
                  // Cuando se encuentra un operador, coloque los dos números en la parte superior
                  // de la pila y use el operador para calcularlos en consecuencia (el siguiente
                  // elemento superior op es el elemento superior de la pila)
                  // (Tenga en cuenta que el cálculo de la expresión de prefijo es el elemento
                  // superior de la pila y el siguiente elemento superior de op)
                  else {
                        int a = resultValues.pop();
                        int b = resultValues.pop();
                        resultValues.push(calculate2(b, a, theChar));
                  }
            }
            return resultValues.pop();
      }

      /**
       * Convierta una expresión infija en una expresión sufija: De manera similar a
       * la conversión a una expresión de prefijo, siga estos pasos: (1) Inicializar
       * dos pilas: pila de operador S1 y pila S2 para almacenar resultados
       * intermedios; (2) Escanee la expresión infija de izquierda a derecha; (3)
       * Cuando encuentre un operando, presiónelo en S2; (4) Cuando se encuentre con
       * un operador, compare su prioridad con el operador de la parte superior de la
       * pila S1: (4-1) Si S1 está vacío, o el operador en la parte superior de la
       * pila es un corchete izquierdo "(", entonces este operador se coloca
       * directamente en la pila; (4-2) De lo contrario, si la prioridad es mayor que
       * la del operador en la parte superior de la pila, el operador también se
       * presiona en S1 (tenga en cuenta que la precedencia es mayor o la misma cuando
       * se convierte a una expresión de prefijo, y la misma situación no se incluye
       * aquí. ); (4-3) De lo contrario, coloque el operador en la parte superior de
       * la pila S1 y empújelo hacia S2, y vaya a (4-1) nuevamente para comparar con
       * el nuevo operador en la parte superior de la pila en S1; (5) Al encontrar
       * paréntesis: (5-1) Si es el corchete izquierdo "(", presione directamente S1;
       * (5-2) Si es el paréntesis derecho ")", los operadores en la parte superior de
       * la pila S1 aparecerán en secuencia, y se presionará S2 hasta que se encuentre
       * el paréntesis izquierdo, en este momento el par de paréntesis se descartará;
       * (6) Repita los pasos (2) a (5) hasta el extremo derecho de la expresión; (7)
       * Muestra los operadores restantes en S1 uno por uno y empújelos en S2; (8)
       * Muestra los elementos en S2 a su vez y salida, y el orden inverso del
       * resultado es la expresión de sufijo correspondiente a la expresión de infijo
       * (el orden inverso no se usa cuando se convierte a la expresión de prefijo).
       * 
       * @return
       */
      private static String toPostfijo(String expression) {

            // (1) Inicializar dos pilas: pila de operadores S1 y pila S2 para almacenar
            // resultados intermedios;
            Stack<Character> ops = new Stack<>();
            Stack<Character> resultValues = new Stack<>();

            char[] chars = expression.toCharArray();

            // (2) Escanea la expresión infija de izquierda a derecha;
            for (char theChar : chars) {
                  // (3) Cuando se encuentra el operando, se presiona en S2;
                  if (Character.isDigit(theChar))
                        resultValues.push(theChar);
                  // (4) Al encontrar un operador, compare su prioridad con el operador en la
                  // parte superior de la pila S1:
                  else if (theChar == '+' || theChar == '-' || theChar == '*' || theChar == '/')
                        dealTheChar2(ops, resultValues, theChar);
                  // (5) Al encontrar paréntesis:
                  // (5-1) Si es el corchete izquierdo "(", entonces presione directamente S1;
                  else if (theChar == '(')
                        ops.push(theChar);
                  // (5-2) Si es el paréntesis derecho ")", los operadores en la parte superior de
                  // la pila S1 aparecerán a su vez, y se presionará S2 hasta que se encuentre el
                  // paréntesis izquierdo, entonces este par de paréntesis se descartará;
                  else if (theChar == ')') {
                        char opsChar = ops.pop();
                        while (opsChar != '(') {
                              resultValues.push(opsChar);
                              opsChar = ops.pop();
                        }
                  }
            }

            // (7) Muestra los operadores restantes en S1 uno por uno y empújelos en S2;
            while (!ops.isEmpty())
                  resultValues.push(ops.pop());

            // (8) Muestra los elementos en S2 a su vez y la salida, y el orden inverso del
            // resultado es la expresión de sufijo correspondiente a la expresión de infijo
            // (no se requiere el orden inverso cuando se convierte a la expresión de
            // prefijo).
            StringBuilder exp = new StringBuilder();
            while (!resultValues.empty())
                  exp.append(resultValues.pop());

            return reverseString2(exp.toString());
      }

      private static void dealTheChar2(Stack<Character> ops, Stack<Character> resultValues, char theChar) {
            // (4-1) Si S1 está vacío, coloque este operador directamente en la pila;
            if (ops.empty()) {
                  ops.push(theChar);
                  return;
            }
            char popTheChar = ops.pop().charValue();
            // (4-1) Si S1 está vacío, o el operador en la parte superior de la pila es el
            // paréntesis derecho "(", entonces este operador se coloca directamente en la
            // pila;
            // (4-2) De lo contrario, si la prioridad es mayor que el operador en la parte
            // superior de la pila, el operador también se presiona en S1 (tenga en cuenta
            // que la prioridad es mayor o la misma cuando se convierte a una expresión de
            // prefijo, y la misma no se incluye aquí Sucediendo)
            if (popTheChar == '(' || getPriorityValue2(theChar) > getPriorityValue2(popTheChar)) {
                  ops.push(popTheChar);
                  ops.push(theChar);
            }
            // (4-3) De lo contrario, coloque el operador en la parte superior de la pila S1
            // y empújelo hacia S2
            else {
                  resultValues.push(popTheChar);
                  // , vaya a (4-1) de nuevo para comparar con el nuevo operador de la parte
                  // superior de la pila en S1;
                  dealTheChar(ops, resultValues, theChar);
            }

      }

      private static int getPriorityValue2(char c) {
      if (c == '+' || c == '-')
      return 0;
      if (c == '*' || c == '/')
      return 1;
            throw new RuntimeException ("operador ilegal");
      }

      private static int calculate2(int a,int b,char c) {
            switch (c){
            case '+':
            return a+b;
            case '-':
            return a-b;
            case '*':
            return a*b;
            case '/':
            return a/b;
            default:
                        throw new RuntimeException ("operador ilegal");
            }
      }

      private static String reverseString2(String string) {

            int n = string.length();
            char[] chars = string.toCharArray();
            for (int i = 0; i < n / 2; i++) {
                  char temp = chars[i];
                  chars[i] = chars[n - 1 - i];
                  chars[n - 1 - i] = temp;
            }
            return String.valueOf(chars);
      }
}