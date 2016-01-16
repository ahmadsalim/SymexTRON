package examples

import syntax.ast._
import syntax.ast.Statement._

/**
  * Created by asal on 15/01/2016.
  */
object IntListExample extends Example {
  override val classDefs: Set[ClassDefinition] = Shared.stdClassDefs ++ Set(
    new ClassDefinition("IntList", Map("next" -> (Class("IntList"), Opt)),
                                   Map("data" -> (Class("Int"), Single)))
  )
  override val pres: Set[SMem] = Set(SMem(Map("list" -> SetLit(), "elem" -> SetLit(Symbol(-2))),
                                     SHeap(Map(-2 -> AbstractDesc(Class("Int"))),Set(),Set())),
                                     SMem(Map("list" -> SetLit(Symbol(-1)), "elem" -> SetLit(Symbol(-2))),
                                     SHeap(Map(-1 -> AbstractDesc(Class("IntList")),
                                               -2 -> AbstractDesc(Class("Int"))), Set(), Set())))
  override val prog: Statement = stmtSeq(
     assignVar("containselem", SetLit())
   , `for`("sublist", MatchStar(SetVar("list"), Class("IntList")), stmtSeq(
        loadField("sublist_data", SetLit(Var("sublist")), "data")
        ,`if`(stmtSeq(), Eq(SetLit(Var("elem")), SetLit(Var("sublist_data"))) -> `new`("containselem", Class("Any")))
    ))
  )
}
