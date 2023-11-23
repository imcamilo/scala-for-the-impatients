package com.github.imcamilo.cats.abstractmath

// import cats._
// import cats.implicits._

object SemiGroups {

  // SEMI GROUPS COMBINE ELEMENTS OF THE SAME TYPE
  import cats.Semigroup
  import cats.instances.int._
  val naturalIntSemigroup = Semigroup[Int] // it has the capability to combine two values of the same type
  val intCombination = naturalIntSemigroup.combine(2, 46)
  // what is the natural combination between 2 integers???
  // THE TCS INSTANCES COMBINE METHOD WILL DO WHATEVER YOUR INTUITION SAYS
  // for ints is an Addition

  import cats.instances.string._
  // for strings is an Concatenation
  val naturalStringSemigroup = Semigroup[String]
  val stringCombination = naturalStringSemigroup.combine("I love Cats bcs it ", "Rocks")

  // Specific API
  def reduceIntsFuncions(list: List[Int]) = list.reduce(naturalIntSemigroup.combine)
  def reduceStringFuncions(list: List[String]) = list.reduce(naturalStringSemigroup.combine)
  /*
  WE CAN DO E.G. list.reduce(_ + _) its simpler right?
  but the power of semigroups is giving us the capability to define very general combinations or reduction API
  regardless what type of elements we are combining
   */

  // general API for reduce "things"
  def reduceAll[C](list: List[C])(implicit semigroup: Semigroup[C]): C = list.reduce(semigroup.combine)

  // TODO 1. SUPPORT NEW TYPES
  case class Expense(id: Long, amount: Double)
  implicit val ExpenseSupport: Semigroup[Expense] = Semigroup.instance((ex1, ex2) => {
    val id = if (ex1.id > ex2.id) ex1.id else ex2.id
    Expense(id, ex1.amount + ex2.amount)
  })

  // extension methods for Semigroup - |+| (combine)

  import cats.syntax.semigroup._
  val anIntSum = 2 |+| 3 // 2 combine 3 // requires the presence of an implicit Semigroup[Int]
  val aStringConcat = "we like " |+| "cats" // requires the presence of an implicit Semigroup[String]
  val aCombinedExpense = Expense(4, 80) |+| Expense(56, 46)

  // TODO 2. IMPLEMENT REDUCE ALL 2, but with the combination function |+|
  def reduceAll2[C](list: List[C])(implicit semigroup: Semigroup[C]): C = list.reduce(_ |+| _)
  def reduceAll3[C: Semigroup](list: List[C]): C = list.reduce(_ |+| _)

  def main(args: Array[String]): Unit = {
    println(intCombination)
    println(stringCombination)

    val ints = (1 to 10).toList
    val strings = List("Im starting ", "to like ", "semigroups")

    // specific API
    println(reduceIntsFuncions(ints))
    println(reduceStringFuncions(strings))

    // general API
    println(reduceAll(strings))
    println(reduceAll(ints))

    //
    import cats.instances.option._
    // compiler will produce an implicit Semigroup[Option[Int]] - combine will produce another option with the summed elements
    // compiler will produce an implicit Semigroup[Option[String]] - combine will produce another option with the concatenate elements
    // the same for any type with an implicit Semigroup

    val numberOptions: List[Option[Int]] = ints.map(a => Option(a))
    println(reduceAll(numberOptions)) // an Option[Int] containing the sum of all nums without unwrapping the options
    val stringOptions: List[Option[String]] = strings.map(a => Option(a))
    println(reduceAll(stringOptions))

    println("custom types")
    val expenses = List(Expense(1, 99), Expense(2, 35), Expense(43, 10))
    println(reduceAll(expenses))
    println(reduceAll2(expenses))
    println(reduceAll3(expenses))
  }

}
