// Fecha: 03/02/2020
// Programador: Francisco Seguí Muñoz
// Asignatura: 15GIIN Estructura de Datos y Algoritmos.
// Unidad: UC1/UC4, Examen Práctico, Ejercicio 2
// Nombre del paquete: resolverEcuaciones.java
// Función: Calcual un sistema de ecuaciones por el método de reducción de Gauss
//          Puede resolver hasta 26 incognitas
//          Puede contener bugs ya que no se han tenido en cuenta absolutamente todos los supestos pero siguiendo las instrucciones debería funcionar correctamente

package com.kiko.resolverecuaciones;

// Importamos el módulo de entrada y salida
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.Arrays; 


/**
 *
 * @author ASUS
 */
public class resolverEcuaciones {

    //	
    // Declaramos algunas funciones genéricas que nos permitirán programar más rápido y con un código más limpio
    //
	
    // Esta función nos permite escribir un texto en consola escribiendo menos y clarificando el código
    static void Print(String texto)
        {
	System.out.print(texto);
    	}
        
    // Esta función es necesaria para escapar el símbolo "+" para que no de problemas con funciones como replace al estar el símbolo en la expresión de busquedda
    public static String EscaparSimboloMas(String cadena)
        {
        String cadenaEscapada="";
        
        for (int n=0;n<cadena.length();n++)
            {
            char c = cadena.charAt(n);
            if (c=='+') cadenaEscapada += "\\+";
            else cadenaEscapada += String.valueOf(c);
            }
        
        return cadenaEscapada;
        }
	
    
    // Esta función permite preguntar al usuario un dato mostrando un texto
    static String InputString(String texto)
	{
	// Mostramos el texto para el usuario
	Print(texto);
		
	// Iniciamos el objeto de entrada por teclado
	Scanner in = new Scanner(System.in);
			
	// Obtenemos el valor introducido por el usuario
        return in.nextLine();
        }
    
    
    // Esta función permite preguntar al usuario un entero mostrando un texto
    static int InputInteger(String texto)
        {
        int valor=0;
        boolean error;
		
        // Iniciamos el objeto de entrada por teclado
        Scanner in = new Scanner(System.in);
			
        // Obtenemos el valor introducido por el usuario
        do  {
            error = false;
            // Mostramos el texto para el usuario
            Print(texto);
            try { // para interceptar exceptiones al convertir los números introducidos
                valor = Integer.parseInt(in.next());
                } catch(NumberFormatException e)
                    {
                   Print("Debes introducir un entero!\n");
                   error = true;
                   }
           } while(error);
            
        // Retornamos el valor introducido por el usuario
        return valor;
        }
        
    
    // Función que elimina los espacios de un String
    public static String EliminaEspacios(String cadena) 
        {
        return cadena.replaceAll("\\s","");
        }
    
    
    // Borra Consola (Simulación)
    public static void Cls()
        {
        for (int n=0;n<32;n++) Print("\n");
        }
    
    
    
    // 
    // Declaramos las las funciones própias del proyecto
    //
    public static boolean VariasSoluciones(String[][] matriz)
        {
        int len = matriz.length;
        double suma = 0;
        for (int n=0;n<len;n++)
            {
            suma = 0;
            for (int m=0;m<len;m++)
                {
                suma=Double.valueOf(matriz[n][m]);
                if (suma!=0) break;
                }
            if (suma==0) return true;
            }
        return false;
        }
    
    
    // Elimina una columna de una matriz
    public static String[][] EliminaColumna(String[][] array,int col)
        {
        int filas = array.length;
        int columnas = array[0].length;
        String newarray[][] = new String[filas][columnas-1];
        
        int j=0;
        for (int f=0;f<filas;f++)
            {
            int k=0;
            for (int c=0;c<columnas;c++)
                {
                if (c==col) continue;
                newarray[j][k] = array[f][c];
                k++;
                }
            j++;
            }
        return newarray;
        }    
    
    
    
    // Despeja las incognitas del sistema
    public static String[][] DespejarIncognitas(String[][] matriz,String[] variables, int incognitas)
        {
        int len = matriz.length-1;
        double resultado = 0;
        double coeficiente = 0;  
        double variableDespejada=0;
        
        int n=incognitas-1;
        // Despeja incognita
        resultado = Double.valueOf(matriz[n][n+1]);
        coeficiente = Double.valueOf(matriz[n][n]);
        variableDespejada = resultado/coeficiente;
        matriz[n][n+1] = String.format("%.1f", variableDespejada);
                
        // Sustituye la incognita en toda la columna de la matriz
        Print(variables[n]+"="+resultado+"/"+coeficiente+" -> "+variables[n]+"="+String.format("%.1f", variableDespejada)+"\n");
                
        for (int m=n-1;m>-1;m--)
            {
            // Sustituye la incognita encontrada en el resto de la matriz
            matriz[m][n] = String.valueOf(Double.valueOf(matriz[m][n])*variableDespejada);       
            
            // Despeja después del igual y elimina la columna de la matriz para poder continuar recursicamente
            resultado = Double.valueOf(matriz[m][n+1]);
            coeficiente = Double.valueOf(matriz[m][n])*-1.0;
            matriz[m][n+1] = String.valueOf(coeficiente+resultado);
            }
        // Elimina la columna de la matriz que tiene la incognita resuelta
        matriz = EliminaColumna(matriz,n);
        
        // Llamada recursiva para seguir despejando incognitas
        incognitas--;
        if (incognitas>0) DespejarIncognitas(matriz,variables,incognitas);
       
        // Termina retornando un array de Strings con las incognitas despejadas
        return  matriz;
        }
    
    
    // Comprueba que dos grupos de variables sean el mismo conjunto
    public static boolean CompruebaVariables(String[] vars1, String[] vars2)
        {
        int len = vars1.length-1;
        int lem = vars2.length-1;
        boolean correcta=false;
        
        for(int n=0;n<len;n++)
            {
            correcta = false;  
            for(int m=0;m<lem;m++)
                {
                if (vars1[n].equals(vars2[m]))
                    {
                    correcta = true; 
                    break;
                    }
                }
            }
            
        if (!correcta) Print("\nLas igualdades deben tener definidas las mismas variables!\n");
        return !correcta;
        }
    
    
    // Multiplica un array a partir de un índice por un coeficiente
    // El array es un string que se convierte a int y luego a String en el momento de multiplicar
    public static String[] MultiplicaArray(String[] array,int mul)
        {
        int len = array.length;
        String[] newarray = new String[len];
        for (int n=0;n<len;n++)
            {
            int tmp = Integer.parseInt(array[n])*mul;
            // Para poner el signo correcto
            newarray[n]="";
            if (tmp>=0) newarray[n]+="+"+String.valueOf(tmp);
            else newarray[n]+=String.valueOf(tmp);
            }
        return newarray;
        }    
  
    
    // Suma dos arrays a partir de un índice
    // El array es un string que se convierte a int y luego a String en el momento de sumar
    // Los arrays deben ser de la misma longitud
    public static String[] SumaArrays(String[] array1,String[] array2)
        {
        int len = array1.length;
        String[] newarray = new String[len];
        for (int n=0;n<len;n++)
            {
            int num1 = Integer.parseInt(array1[n]);
            int num2 = Integer.parseInt(array2[n]);
            int tmp = num1+num2;
            // Para poner el signo correcto
            newarray[n]="";
            if (tmp>=0) newarray[n]+="+"+String.valueOf(tmp);
            else newarray[n]+=String.valueOf(tmp);
            } 
        return newarray;
        }
    
    
    	// Triangula una matriz dejando los coeficientes de abajo a cero
    public static String[][] TiangularMatriz(String[][] matriz,String[] variables)
        {
        int len=matriz.length;
        // Si solo existe una incognita retorna lo mismo porque la ecuación se puede resolver
        if (len<1) return matriz;
        
        String[] igualdadSuperior = new String[matriz[0].length];
        String[] igualdadSiguiente = new String[matriz[0].length];
        
        // Para mtriangular una matriz debemos reducir el primer coeficiente de cada fila con el primer coeficiente de la primera
        // Luego debemos reducir el segundo coeficiente a partir de la tercera con el coeficiente de la segunda, y así sucesivamente
        
        for (int m=0;m<len-1;m++)
            {
            int N1 = Integer.parseInt(matriz[m][m]); // Para triangular, en cada pasada el coeficiente será el siguiente de la igualdad
            
            for (int n=m+1;n<len;n++)
                {
                int N2 = Integer.parseInt(matriz[n][m]);
                             
                // Multiplica los coeficientes de la igualdad superior por N2
                igualdadSuperior = MultiplicaArray(matriz[m],N2);
                // Multiplica los coeficientes de las siguientes igualdades de la matriz por N1
                igualdadSiguiente = MultiplicaArray(matriz[n],N1);
               
                
                 // Comprueba si se ha reducido el coeficiente, sino cambia el signo para la reducci
                double tmp = Double.valueOf(igualdadSuperior[m])+Double.valueOf(igualdadSiguiente[m]);
                if (tmp!=0)
                    {
                    N2*=-1;
                    // Multiplica los coeficientes de la igualdad superior por N2
                    igualdadSuperior = MultiplicaArray(matriz[m],N2);
                    // Multiplica los coeficientes de las siguientes igualdades de la matriz por N1
                    igualdadSiguiente = MultiplicaArray(matriz[n],N1);
                    }    
                // Sumamos las igualdades y ponemos el resultado en la igualdad actual
                matriz[n] = SumaArrays(igualdadSuperior,igualdadSiguiente);             
                }
            }
        return matriz;
        }
    
    
    // Muestra un array de strings en consola
    public static void MuestraArray(String array[],boolean comoIgualdad,int espacios)
        {
            
        // Muestra un array en consola
        int len = array.length;
        if (!comoIgualdad) 
            {
            // Muestra el array como términos independientes
            for (int n=0;n<len;n++) Print(" ".repeat(espacios-array[n].length())+array[n]);
            }
        else
            {
            // Muestra el array como una igualdad
            int n=0;
            for (;n<len-1;n++) Print(array[n]);
            Print("="+array[n]);
            }
        Print("\n");
        }
    
    
    // Muestra una matriz en una tabla
    public static void MuestraMatriz(String matriz[][],String variables[])
        {
        Print("\nMatriz:\n");
        // Calcula el término que ocupa más espacio y el ancho total
        int anchoTotal = 0;
        int max = -1;
        int ln = matriz.length;
        for (int n=0;n<ln;n++)
            {
            int lm = matriz[n].length;
            for (int m=0;m<lm;m++)
                {
                int ancho = matriz[n][m].length()+1;
                anchoTotal += ancho;
                if (ancho>max) max = ancho;
                }
            }
        
        // Muestra las variables
        MuestraArray(variables,false,max);
        
        // Muestra una linea horizontal del tamaño adecuado
        Print("-".repeat((max*(ln+1))+1)+"\n");
        
        // Muestra un array en consola
        for (int n=0;n<ln;n++) MuestraArray(matriz[n],false,max);
        }
    
    
    // Si el orden de las variables de la igualdad actual es diferente al de la primera igualdad introducida por el usuario las reorganiza
    public static String[] ReorganizaTerminos(String terminos[],String variables[])
        {
        int lent = terminos.length;
        int lenv = variables.length;
        String terms[] = new String[lent];
        
        // El último termino no tiene variable
        terms[lent-1] = terminos[lent-1];
        for (int n=0;n<lenv;n++)
            {
            // Busca en todos los términos el que contenga la variable para guardarlo en el mismo orden
            for (int m=0;m<lenv;m++) if (terminos[m].contains(variables[n])) terms[n] = PonSigno(terminos[m]);
            }
        
        // Si hay cambios muestra la igualdad reorganizada 
        if (!Arrays.deepEquals(terms, terminos))
            {
            Print("Igualdad reorganizada para que las variables estén en el mismo orden -> ");
            MuestraArray(terms,true,1);
            }
        
        // Retorna los terminos reorganizados
        return terms;
        }

    
    // Función que comprueba que un string contenga un número máximo de carácteres de un conjunto y sin repeticiones
    // Además comprueba que los carácteres del string pertenezcan al conjunto de variables
    // Se ignoran los números, y los carácteres "+-/." que forman parte de una posible expresión
    public static boolean VariablesCorrectas(String cadena,String conjunto, int variables)
        {
        boolean PerteneceAlConjunto = true;
        int repeticiones;
        int contador = 0;
        String operadores = "+-/=";
        String numeros = "0123456789.";
        
        // Comprueba si los caracteres de la cadena pertenecen al conjunto de variables permitidas
        for (int n=0;n<cadena.length();n++)
            {
            // Ignora los operadores y números
            if (operadores.contains(String.valueOf(cadena.charAt(n)))) continue;
            if (numeros.contains(String.valueOf(cadena.charAt(n)))) continue;
            // Si el caracter no pertenece al conjunto de variables retorna false
            if (!conjunto.contains(String.valueOf(cadena.charAt(n))))
                {
                Print("Para una igualdad debes utilizar caracteres validos del conjunto ("+conjunto+")\n");
                return false;
                }        
            }
        // Comprueba si un caracter del conjunto de incognitas no está repetido más de una vez en la cadena (excepto +-/.)
        for (int n=0;n<conjunto.length();n++)
            {
            repeticiones = 0;
            for (int m=0;m<cadena.length();m++)
                {
                if (cadena.charAt(m)==conjunto.charAt(n)) repeticiones++;
                if (repeticiones>1)
                    {
                    Print("En una igualdad una incognita solo se puede aparecer una vez!\n");
                    return false;
                    }
                }
            }
        // Comprueba el número de incognitas definidas
        for (int n=0;n<conjunto.length();n++)
            {
            for (int m=0;m<cadena.length();m++)
                {
                if (cadena.charAt(m)==conjunto.charAt(n)) contador++;
                }
            }
        // El número de incognitas debe ser igual al introducido por el usuario previamente
        if (contador!=variables)
            {
            Print("El numero de incognitas de la igualdad no es correcto!\n");
            return false;
            }
        
        // Comprueba que después del igual existe un término o variable
        
        return true;
        }
    
   
    // Esta función comprueba si en una igualdad hay alguna expresión no permitida
    public static boolean ExpresionNoPermitida(String igualdad, String conjunto, int variables)
        {
        // Comprueba expresiones incorrectas (o al menos la mayoría)
        boolean noPermitida = igualdad.contains("..")||igualdad.contains("//")||igualdad.contains("++")||igualdad.contains("--")||igualdad.contains("+-")||igualdad.contains("*")||igualdad.contains("+=")||igualdad.contains("-=");
        
        // Si la igualdad no contiene un "=" no es válida
        if (!igualdad.contains("="))
            {
            Print("Falta el igual!\n");
            return true;
            }
        
        // Si los términos no tienen los operadores + o - correctos muestra el error correcpondiente
        if (noPermitida)
            {
            Print("Has iintroducido una igualdad incorrecta!\n");
            return true;
            }
        
        int count = igualdad.length() - igualdad.replace("=", "").length();        
        // Si el símbolo "=" no está correcto visualiza el error y retorna true indicando que la igualdad no es correcta
        if (count>1)
            {
            Print ("En una igualdad solo puede existir un solo simbolo '=' !!\n");
            return true;
            }
            
        // Comprueba que el útilo caracter sea una variable o un número
        count = igualdad.length()-1;
        if ((igualdad.charAt(count)=='+')||(igualdad.charAt(count)=='-')||(igualdad.charAt(count)=='='))
            {
            Print ("Igualdad con terminación incorrecta!!\n");
            return true;
            }
        
        // Comprueba que tenemos tantas variables como incognitas hemos definido y que no hay repeticiones
        // También se comprueba que las variables sean las admitidas
        return !(VariablesCorrectas(igualdad,conjunto,variables));
        }
     
   
    // Obtiene todos los términos de una igualdad
    // Retorna un array de términos de una igualdad, le último término es el que vá despues del igual
    public static String[] ObtenerTerminos(String igualdad,String conjunto, int incognitas)
        {
        String terminos[] = new String[incognitas];
        String temp;
        int count = 0;
        
        // Busca los terminos de las incognitas más el término independiente
	for (int m=0;m<incognitas;m++)
            {
            // Primero el termino que está despues del igual
            for (int n=0;n<igualdad.length();n++)
		{
                if (n==0) continue;

                if (igualdad.charAt(0)=='=')
                    {
                    igualdad = igualdad.replaceFirst("=","");
                    terminos[count] = PonSigno(igualdad); 
                    break;
                    }

		if ((igualdad.charAt(n)=='+')||(igualdad.charAt(n)=='-')||(igualdad.charAt(n)=='='))
                    {
                    temp = igualdad.substring(0,n);
                    terminos[count] = PonSigno(temp); igualdad = igualdad.replaceFirst(EscaparSimboloMas(temp),"");
                    count++;
                    break;
                    }
                }
            }
        
        return terminos;
        }
    
    
        // Comprueba si un término es independiente (que no tiene variable)
        public static boolean EsUnTerminoIndependiente(String termino, String conjunto)
            {
            for (int n=0;n<termino.length();n++)
                if (conjunto.contains(String.valueOf(termino.charAt(n)))) return false;
            return true;
            }
    
	
	// Retorna el índice del termino independiente
	public static int ObtenerTerminoIndependiente(String terminos[],String conjunto)
            {
            int indice = -1;
            for (int n=0;n<terminos.length;n++) if (EsUnTerminoIndependiente(terminos[n],conjunto)) return n;		
            return indice;
            }
	
	
	// Cambia signo de un término
	public static String CambiaSigno(String termino)
            {
            char[] Chars = termino.toCharArray();
          
            if(Chars[0]=='-') 
                {
                Chars[0]='+';
                return String.valueOf(Chars);        
                }
            else
                {
                if(Chars[0]=='+')
                    {
                    Chars[0]='-';
                    return String.valueOf(Chars);
                    }
                }
            return "+"+termino;
            }
	
        
        // Cambia signo de un término
	public static String PonSigno(String termino)
            {
            char[] Chars = termino.toCharArray();
          
            if ((Chars[0]=='-')||(Chars[0]=='+')) 
                return String.valueOf(Chars);        
                
            return "+"+termino;
            }
	
        
    // Función que comprueba si hay un término con variable después del igual de una igualdad y retorna la igualdad cambiandolo
    // Ejemplo: Si la igualdad introducida es 3x+2y-4=z retorna 3x+2y-z=4
    public static String[] OrdenaTerminoIndependiente(String terminos[],String conjunto)
        {
        int len = terminos.length-1;
	// Si después del igual no hay un término independiente debe burcarlo en la igualdad y sustiruirlo
        if (!EsUnTerminoIndependiente(terminos[len],conjunto))
            {
            // Cambia el signo del termino después del igual
            String despuesDeIgual = CambiaSigno(terminos[len]);
            // Obtiene el índice del termino independiente
            int indice = ObtenerTerminoIndependiente(terminos,conjunto);
            
            // Cambia el signo del término independiente
            String terminoIndependiente = CambiaSigno(terminos[indice]);
            
            // Intercambia el termino independiente con el término que hay después del igual
            terminos[indice]  = despuesDeIgual;
            terminos[len] = terminoIndependiente;
            
            // Muestra la nueva igualdad en consola
            String igualdad = "";
            int n=0;
            for (;n<len;n++) igualdad += terminos[n];
            igualdad += "="+terminos[n];
            Print("Moviendo termino independiente -> "+igualdad+"\n");
            }
		
        return terminos;
        }
    
    
    // Obtiene un termino sin la variable
    // Retorna un String con el termino
    public static String ObtieneTerminoSinVariable(String termino, String conjunto)
        {
        int len = termino.length();
        String temp="";
        for (int n=0;n<len;n++) if (!conjunto.contains(String.valueOf(termino.charAt(n)))) temp += String.valueOf(termino.charAt(n));
        
        // Si solo tenia letra le pone 1
        if (temp.equals("+")||temp.equals("-"))temp+="1";
        if (temp.equals("")) temp = "+1";
        
        return temp;
        }
    
    
    // Obtiene todos los términos de una igualdad sin las variables
    // Retorna un array con los terminos sin las variables
    public static String[] ObtieneTerminosSinVariables(String terminos[],String conjunto)
        {
        int len = terminos.length;
        String terms[] = new String[len];
        for (int n=0;n<len;n++)
            {
            terms[n]=ObtieneTerminoSinVariable(terminos[n],conjunto);
            // Si el término no tiene algún número es que la variable estaba sola, entonces le pone un 1
            if (!Pattern.compile("[0-9]").matcher(terms[n]).find()) terms[n]= terms[n].replace("["+conjunto+"]", "1");
            }
        return terms;
        }

    
    // Obtiene la variable de un término
    // Retorna un String con la variable
    public static String ObtieneVariableTermino(String termino, String conjunto)
        {
        int len = termino.length();
        for (int n=0;n<len;n++) if (conjunto.contains(String.valueOf(termino.charAt(n)))) return String.valueOf(termino.charAt(n));
        return "";
        }
    
    
    // Obtiene las variables de un array de términos
    // Retorna una array de strings con las variables de cada término
    public static String[] ObtieneVariables(String terminos[],String conjunto)
        {
        int len = terminos.length;
        String variables[] = new String[len];
        for (int n=0;n<len;n++)
            variables[n]=ObtieneVariableTermino(terminos[n],conjunto);
        return variables;
        }
    
    
    // Solicita al usuario que introduzca el número de incognitas que tendrá el sistema a resolver
    public static int PideAlUsuarioLasIncognitas()
        {
        int incognitas;
        
        do {
            incognitas = InputInteger("Introduce el numero de incognitas del sistema de ecuaciones a resolver: ");
            if (incognitas<1) Print("Debes introducir por lo menos una incognita!\n");
            if (incognitas>26) Print("Puedes introducir como máximo 26 incognitas!\n");
            } while((incognitas<1)||(incognitas<1));
        return incognitas;
        }
      
    
    // Muestra una ayuda al usuario
    public static void Ayuda()
        {
        String ayuda = InputString("¿Quieres ver la ayuda? (s/n) ");
        if ((ayuda.equals("s"))||(ayuda.equals("S")))
            {
            Print("Este programa intenta resolver sistemas de ecuaciones por el metodo de reduccion de Gauss.\n");
            Print("Teoricamente puede solucionar sistemas desde 1 a 26 incognitas.\n");
            Print("Pasos a seguir para resolver un sistema de ecuaciones:\n");
            Print("     - Se le pedira al usuario el número de incognitas del sistema.\n");
	    Print("     - Para resolverlo el usuario deberá proporcionar tantas igualdades como incognitas haya definido.\n");
            Print("     - Si las igualdades introducidas no permiten solucionar el sistema, el resultado será imprevisible.\n");
            Print("     - Cada igualdad debe contener el numero de incognitas definida anteriormente.\n");
            Print("     - Las incognitas se pueden definir con las letras minusculas del conjunto 'abcdefghijklmnopqrstuvwxyz' (no incluye ñ).\n");
            Print("     - Las igualdades deben tener definidas las mismas incognitas.\n");
            Print("     - Si las igualdades no tienen las incognitas en el mismo orden que la primera, se reorganizarán en el mismo orden.\n");
            Print("     - Como minimo debe existir una variable a la izquierda del '='.\n");
            Print("     - Cada igualdad debe tener un termino independiente.\n");
            Print("     - Ejemplo de una igualdad para un sistema de 2 incognitas: 2x+y=8.\n");
            Print("     - Si falta alguna inconita se le indicará al usuario para que introduzca nuevamente la igualdad.\n");
            Print("     - Si se introduce una igualdad con el termino independiente antes del igual el sistema lo intercambiara.\n");
            Print("             Ejemplo: 2x-8=-y -> 2x+y=8.\n");
            Print("     - Si las igualdades no tiene un termino independiente se le indicara al usuario para que la introduzca correctamente.\n");
            Print("     - Si se introducen igualdades con las variables en diferente orden que la primera, seran reorganizadas para poder realizar la reduccion correctamente.\n");
            Print("     - Pueden usarse los operadores + - entre terminos pero no la división o fracciones.\n");
            Print("     - Una vez introducidas todas las igualdades el sistema crea una matriz para intentar resolverla.\n");
            Print("     - Utilizando el metodo de reducción de Gauss intentara llegar a una matriz triandular arriba con ceros abajo.\n");
            Print("     - Para un sistema con varias o infinitas soluciones puede dar una de ellas o no llegar resolverlo.\n");
            // Pide al usuario pulse intro para continuar
            InputString("\nPulsar Intro para continuar.\n");
            }
        }
    
	
    
    // Muestra un texto inicial
    public static void Leyenda()
        {
        Print("Resolucion de Ecuaciones por el metodo de Reduccion de Gauss.\n\n");
        Print("Puedo resolver sistemas de 1 a 26 incognitas!!\n\n");
        }
		
    
    
	
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
        {
        // Incognitas de la ecuación
        int incognitas;
        // Igualdades introducidas por el usuario
        String igualdades[] = {""};
        // Variables y operadores que podemos encontrar
        String conjuntoVariables = "abcdefghijklmnopqrstuvwxyz";
        // Matriz para resolver el sistema
        String[][] matriz;
        // Para almacenar las igualdades que va introduciendo el usuario
        String igualdad;
        // Almacena los términos después de introducir una igualdad
        String terminos[];
        // Almacena los términos sin las variables después de introducir una igualdad
        String terminosSinVariables[];
        // Almacena solo las variables
        String variablesSolas[];
        
        
        // Borra la consola
        Cls();
        // Muestra un texto para el usuario
        Leyenda();
        // Pide al usuario si quiere ver la ayuda
        Ayuda();
        
        // Solicita el número de incognitas del sistema de ecuaciones
        incognitas = PideAlUsuarioLasIncognitas();
        
        // Definimos un array que contendrá tandas igualdades como incónitas tanga la ecuación para poder serolverla
        igualdades = new String[incognitas];
        
        // Define la matriz que se usará para resolver el sistema de ecuaciones
        matriz = new String[incognitas][incognitas+1];
        // Define la matriz almacenará las variables
        variablesSolas = new String[incognitas];
        
        // Pide al usuario que vaya introduciendo tantas igualdades como incognitas del sistema
        // Loop según el número de incognitas
        for (int n=0;n<incognitas;n++)
            {
            boolean expresionIncorrecta = true;
            // Loop que se repite si la igualdad introducida no es correcta solicitándola otra vez
            while (expresionIncorrecta)
                {
                // Solicita al usuario que introduzca una igualdad
                igualdad = InputString("Introduce la igualdad "+(n+1)+": ");
                
                // Sale de la aplicación durante la introducción de igualdades pulsando intro sin texto
                
                // Elimina los espacios que puedan existir en la igualdad
                igualdad = EliminaEspacios(igualdad);
                Print(igualdad+"\n");
                
                // Comprueba que la igualdad es correcta
                expresionIncorrecta = ExpresionNoPermitida(igualdad,conjuntoVariables,incognitas);
                
                // Si la expresión está permitida
                if (!expresionIncorrecta)
                    {
                    // Obtiene los términos de la igualdad por separado en un array
                    terminos = ObtenerTerminos(igualdad,conjuntoVariables,incognitas+1);
                                  
                    // Si la igualdad introducida no tiene un término independiente después del igual,
                    // busca el termino independiente y lo intercambia con el termino por el que hay después del igual cambiando los signos.
                    terminos = OrdenaTerminoIndependiente(terminos,conjuntoVariables);
                    
                    // Elimina las variables de cada término para poder incluirlos en la matriz
                    terminosSinVariables = ObtieneTerminosSinVariables(terminos,conjuntoVariables); // Si una variable está sola le pone un 1
                    
                    if (n==0)
                        {
                        // Obtiene las variables solas
                        variablesSolas = ObtieneVariables(terminos,conjuntoVariables);
                        }
                    else
                        {
                        // Comprobar que no existen variables diferentes
                        expresionIncorrecta = CompruebaVariables(variablesSolas,ObtieneVariables(terminos,conjuntoVariables));
                        
                        if (!expresionIncorrecta)
                            {
                            // Ordenar en el mismo orden las expresiones según la variable para que coincidan todas con la primera igualdad
                            // Si el orden de las variables es diferente reordena los terminos y las variables
                            terminos = ReorganizaTerminos(terminos,variablesSolas);
                            
                            // Elimina las variables de cada término para poder incluirlos en la matriz
                            terminosSinVariables = ObtieneTerminosSinVariables(terminos,conjuntoVariables); // Si una variable está sola le pone un 1
                            }
                        }
                    
                    if (!expresionIncorrecta)
                        {
                        // Guardar terminos en la matriz
                        matriz[n] = terminosSinVariables;
                        igualdades[n] = igualdad;
                        }
                    }
                }

            }
        
        // Muestra muestra variables y matriz
        MuestraMatriz(matriz,variablesSolas); 
        
        
        Print("\nPara solucionar el sistema aplicamos la reducción por el método de Gauss\n");
        Print("Para ello debemos triangular la matriz con ceros abajo.\n");
        
        // Procesa la matriz de términos para resolver el sistema mediante la reducción de gauss
        Print("Iniciando el proceso de reducción por el método de Gauss...\n\n");
            
        
        // Tiangular la matriz
        String[][] matrizTriangulada;
        matrizTriangulada = TiangularMatriz(matriz,variablesSolas);
        
        Print("Matriz Triangulada:\n");
        // Muestra muestra variables y matriz triangulada
        MuestraMatriz(matrizTriangulada,variablesSolas);
        
        // Comprueba si tiene varias soluciones
        if (VariasSoluciones(matrizTriangulada))
            Print("\nEl sistema tiene mas de una solucion, se intentara llagar a una de ellas.\n");
        
        Print("\nSolucion encontrada:\n");
        // Despejar incognitas
        DespejarIncognitas(matrizTriangulada,variablesSolas,incognitas);
        }
    }