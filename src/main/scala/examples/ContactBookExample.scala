package examples

import semantics.domains._
import syntax.ast.Statement._
import syntax.ast._

/**
  * Created by asal on 15/01/2016.
  */
object ContactBookExample extends Example {
  override val excludedBranches = Set(BranchPoint(7,2))

  override val classDefs: Set[ClassDefinition] = Shared.stdClassDefs ++ Set(
    new ClassDefinition("ContactBook", Map("persons" -> (Class("Person"), Many)), Map())
  , new ClassDefinition("Person", Map("age" -> (Class("Age"), Single),
                                      "name" -> (Class("String"), Single)), Map())
  , new ClassDefinition("Age", Map(), Map())
  , new ClassDefinition("Adult", Map(), Map(), Some(Class("Age")))
  , new ClassDefinition("Child", Map(), Map(), Some(Class("Age")))
  , new ClassDefinition("Invited", Map("name" -> (Class("String"), Single)), Map())
  )
  override val pres: Set[SMem] = {
    val stack = Map("contactbook" -> SetLit(Seq(Symbol(-1))))
    Set(
      SMem(stack, stack,
        SHeap.initial(Map(), Map(Symbol(-1) -> UnknownLoc(Class("ContactBook"), SUnowned, Set())), Map(), Map(), Set()))
    )
  }

  override val prog: Statement = stmtSeq(
     assignVar("invitationlist", SetLit(Seq()))
   , `for`("person", MatchStar(Var("contactbook"), Class("Person")), stmtSeq(
      assignVar("isadult", SetLit(Seq()))
    , loadField("person_age", Var("person"), "age")
    , loadField("person_name", Var("person"), "name")
    , `for`("age", Match(Var("person_age"), Class("Adult")), stmtSeq(
        `new`("isadult", Class("Any"))
      ))
    , `if`(Not(Eq(Var("isadult"), SetLit(Seq())))
        , stmtSeq(
            `new`("invited", Class("Invited"))
          , assignField(Var("invited"), "name", Var("person_name"))
          , assignVar("invitationlist", Union(Var("invitationlist"), Var("invited"))))
        , stmtSeq())
    ))
  )
}
