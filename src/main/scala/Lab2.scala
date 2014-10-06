object Lab2 extends jsy.util.JsyApplication {
  import jsy.lab2.Parser
  import jsy.lab2.ast._
  
  /*
   * CSCI 3155: Lab 2
   */

  /*
   * Replace the 'throw new UnsupportedOperationException' expression with
   * your code in each function.
   * 
   * Do not make other modifications to this template, such as
   * - adding "extends App" or "extends Application" to your Lab object,
   * - adding a "main" method, and
   * - leaving any failing asserts.
   * 
   * Your lab will not be graded if it does not compile.
   * 
   * This template compiles without error. Before you submit comment out any
   * code that does not compile or causes a failing assert.  Simply put in a
   * 'throws new UnsupportedOperationException' as needed to get something
   * that compiles without error.
   */
  
  /* We represent a variable environment is as a map from a string of the
   * variable name to the value to which it is bound.
   * 
   * You may use the following provided helper functions to manipulate
   * environments, which are just thin wrappers around the Map type
   * in the Scala standard library.  You can use the Scala standard
   * library directly, but these are the only interfaces that you
   * need.
   */
  
  type Env = Map[String, Expr]
  val emp: Env = Map()
  def get(env: Env, x: String): Expr = env(x)
  def extend(env: Env, x: String, v: Expr): Env = {
    require(isValue(v))
    env + (x -> v)
  }
  
  /* Some useful Scala methods for working with Scala values include:
   * - Double.NaN
   * - s.toDouble (for s: String)
   * - n.isNaN (for n: Double)
   * - n.isWhole (for n: Double)
   * - s (for n: Double)
   * - s format n (for s: String [a format string like for printf], n: Double)
   */

  def toNumber(v: Expr): Double = {
    require(isValue(v))
    (v: @unchecked) match {
      case N(n) => n
      case B(b) => if(b) 1 else 0
      case S(s) => try { s.toDouble } catch { case _: Throwable => Double.NaN }
      case Undefined => Double.NaN
      case null => 0
      case _ => throw new UnsupportedOperationException
    }
  }
  
  def toBoolean(v: Expr): Boolean = {
    require(isValue(v))
    (v: @unchecked) match {
      case B(b) => b
      case N(n) => {
        if(n.isNaN) false 
        else if(n == -0 | n == +0) false
        else true
      }
      case S(str) => if(str.length == 0) false else true
      case Undefined => false
      case null => false
      case _ => throw new UnsupportedOperationException
    }
  }
  
  def toStr(v: Expr): String = {
    require(isValue(v))
    (v: @unchecked) match {
      case S(s) => s
      case B(b) => b.toString
      case N(n) => if (n.isWhole) "%.0f" format n else n.toString
      case null => "null"
      case Undefined => "undefined"
      case _ => throw new UnsupportedOperationException
    }
  }
  
  def eval(env: Env, e: Expr): Expr = {
    /* Some helper functions for convenience. */
    def eToVal(e: Expr): Expr = eval(env, e)

    e match {
      /* Base Cases */
      case N(n) => N(n)
      case S(s) => S(s)
      case B(b) => B(b)
      case Undefined => Undefined
      case Var(x) => get(env, x)
      case ConstDecl(x, e1, e2) => 
        val constEnv: Env = extend(env, x, eToVal(e1))
        eval(constEnv, e2)

        
      case Binary(bop, e1, e2) =>{
        val v1 = eToVal(e1)
        val v2 = eToVal(e2)
        bop match{
          
          case And => if(!toBoolean(v1)) v1 else v2
          case Or => if(toBoolean(v1)) v1 else v2
          case Plus => {
        	  (v1, v2) match{
	          	case (S(s1), S(s2)) => S(s1 + s2)
	      	  	case (S(s1), N(n2)) => S(s1 + toStr(v2)) //if string is either
	      	  	case (N(n1), S(s2)) => S(toStr(v1) + s2) //concatenate as str
	            case _ => N(toNumber(v1)+toNumber(v2))
        	  }
          }
          case Minus => N(toNumber(v1)-toNumber(v2))
          case Times => N(toNumber(v1)*toNumber(v2))
          case Div => N(toNumber(v1)/toNumber(v2))
          case Eq => (v1, v2)match {
	      	  case (N(n1), N(n2)) => B((n1 == n2))
	      	  case (S(s1), S(s2)) => B((s1 == s2)) // Not tested
	      	  case (B(b1), B(b2)) => B((b1 == b2)) // Not tested
	      	  case _ => B(false) //Non equal types
	      }
          case Ne => (v1, v2) match { // We need to write this to pass "4" == 4
	      	  case (N(n1), N(n2)) => B(!(n1 == n2))
	      	  case (S(s1), S(s2)) => B(!(s1 == s2)) // Not tested
	      	  case (B(b1), B(b2)) => B(!(b1 == b2)) // Not tested
	      	  case _ => B(false) //Non equal types
	      }
          case Lt => (v1, v2)match {
	      	  case (N(n1), N(n2)) => B(n1 < n2)
	      	  case (S(s1), S(s2)) => B(s1 < s2)
	      	  case (B(b1) , B(b2)) => B(b1 < b2)
	      	  case _ => B(toNumber(v1) < toNumber(v2))
	      }
          case Le => (v1, v2) match {
	      	  case (N(n1), N(n2)) => B(n1 <= n2)
	      	  case (S(s1), S(s2)) => B(s1 <= s2)
	      	  case (B(b1) , B(b2)) => B(b1 <= b2)
	      	  case _ => B(toNumber(v1) <= toNumber(v2))
	      }
          case Gt => (v1, v2)match {
	      	  case (N(n1), N(n2)) => B(n1 > n2)
	      	  case (S(s1), S(s2)) => B(s1 > s2)
	      	  case (B(b1) , B(b2)) => B(b1 > b2)
	      	  case _ => B(toNumber(v1) > toNumber(v2))
	      }
          case Ge => (v1, v2) match {
	      	  case (N(n1), N(n2)) => B(n1 >= n2)
	      	  case (S(s1), S(s2)) => B(s1 >= s2)
	      	  case (B(b1) , B(b2)) => B(b1 >= b2)
	      	  case _ => B(toNumber(v1) >= toNumber(v2))
	      }
          case Seq => eToVal(v2)
          case _ => throw new UnsupportedOperationException
        }
      }
      /*case ConstDecl(x, e1, e2) => {
        e2 match{
          case Binary(bop, eb1, eb2) => eToVal(Binary(bop, e1, eb2))
          case _ => throw new UnsupportedOperationException
        }
      }*/
      
      case If(e1, e2, e3) => {
        if(toBoolean(e1)){
          eToVal(e2)
        }
        else{
          eToVal(e3)
        }
      }
      case Unary(uop, e1) =>{
        uop match{
          case Neg => N(-toNumber(e1))
          case Not => B(!toBoolean(e1))
          case _ => throw new UnsupportedOperationException
        }
      }
      
      

      
      /* Inductive Cases */
      case Print(e1) => println(pretty(eToVal(e1))); Undefined

      case _ => throw new UnsupportedOperationException
    }
  }
    
  // Interface to run your interpreter starting from an empty environment.
  def eval(e: Expr): Expr = eval(emp, e)

  // Interface to run your interpreter from a string.  This is convenient
  // for unit testing.
  def eval(s: String): Expr = eval(Parser.parse(s))

 /* Interface to run your interpreter from the command-line.  You can ignore what's below. */ 
 def processFile(file: java.io.File) {
    if (debug) { println("Parsing ...") }
    
    val expr = Parser.parseFile(file)
    
    if (debug) {
      println("\nExpression AST:\n  " + expr)
      println("------------------------------------------------------------")
    }
    
    if (debug) { println("Evaluating ...") }
    
    val v = eval(expr)
    
    println(pretty(v))
  }

}