package examples

import semantics.domains._
import syntax.ast._
import syntax.ast.Statement._

/**
  * Created by asal on 15/01/2016.
  */
trait IntListExample extends Example {
  override val classDefs: Set[ClassDefinition] = Shared.stdClassDefs ++ Set(
    new ClassDefinition("IntList", Map("next" -> (Class("IntList"), Opt)),
                                   Map("data" -> (Class("Int"), Single)))
  )

}

object IntListContainsElementExample extends IntListExample {
  override val pres: Set[SMem] = {
    val stack = Map("list" -> SetSymbol(-1), "elem" -> SetLit(Seq(Symbol(-2))))
    Set(
      SMem(stack, stack,
        SHeap.initial(Map(SetSymbol(-1) -> SSymbolDesc(Class("IntList"), Opt, SUnowned)), Map(Symbol(-2) -> UnknownLoc(Class("Int"), SUnowned)), Map(), Map(), Set()))
    )
  }
  override val prog: Statement = stmtSeq(
    assignVar("containselem", SetLit(Seq()))
    , `for`("sublist", MatchStar(SetVar("list"), Class("IntList")), stmtSeq(
      loadField("sublist_data", SetLit(Seq(Var("sublist"))), "data")
      ,`if`(Eq(SetLit(Seq(Var("elem"))), SetLit(Seq(Var("sublist_data"))))
        , `new`("containselem", Class("Any"))
        , stmtSeq())
    ))
  )
}

object IntListHeadTailEqExample extends IntListExample {
  override val pres: Set[SMem] = {
    val stack = Map("list" -> SetSymbol(-1))
    Set(
      SMem(stack, stack,
        SHeap.initial(Map(SetSymbol(-1) -> SSymbolDesc(Class("IntList"), Opt, SUnowned)), Map(), Map(), Map(), Set()))
    )
  }
  override val prog: Statement = `if`(Eq(SetVar("list"), SetLit(Seq())),
      `new`("res", Class("Any"))
    , stmtSeq(
        loadField("head", SetVar("list"), "data")
      , loadField("list_next", SetVar("list"), "next")
      , `if`(Eq(SetVar("list_next"), SetLit(Seq())),
          `new`("res", Class("Any"))
        , stmtSeq(fix(SetVar("list_next"), stmtSeq(
             loadField("list_next_next", SetVar("list_next"), "next")
          , `if`(Eq(SetVar("list_next_next"), SetLit(Seq())),
                loadField("tail", SetVar("list_next"), "data")
              , assignVar("list_next", SetVar("list_next_next"))
            ))
          )
        , `if`(Eq(SetVar("head"), SetVar("tail")),
            `new`("res", Class("Any")),
             assignVar("res", SetLit(Seq()))
           ))
        )
      )
    )
}
