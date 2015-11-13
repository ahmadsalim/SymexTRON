package syntax

import helper._
import scalaz._, Scalaz._

package object ast extends SymbolicOps {
  type Vars = String
  type Symbols = Int
  type Fields = String
  type Spatial[T] = Map[T, SpatialDesc]
  type Prop = Set[BoolExpr]
  type SStack = Map[Vars, SetExpr]
  type Instances = Int
  type CStack = Map[Vars, Set[Instances]]

  def not(p : BoolExpr) : BoolExpr = p match {
    case Not(Not(p)) => not(p)
    case Not(p) => p
    case p => Not(p)
  } //Smart constructor

  implicit class RichDefs(defs: Map[Class, ClassDefinition]){
    val childfields: Set[Fields] = defs.values.flatMap(_.children.keys).toSet
    val reffields: Set[Fields]   = defs.values.flatMap(_.refs.keys).toSet

    // Consolidate supertypes and subtypes fields

    def fieldType(c : Class, f : Fields): Option[(Class, Cardinality)] =
      (Set(c) ++ supertypes(c)).map(defs).find(cdef =>
        cdef.children.contains(f) || cdef.refs.contains(f)
      ).flatMap(cdef => cdef.children.get(f)
                            .orElse(cdef.refs.get(f)))

    def supertypes(c : Class): Set[Class] =
     (if (c == Class("Nothing")) defs.keys.toSet
     else defs(c).superclass.toSet.|>(s =>
      s ++ s.flatMap(supertypes _)
    )) + Class("Any")

    val subtypes: Map[Class, Set[Class]] = defs.mapValues(_ => Set[Class]()) ++ {
      defs.values.foldLeft(Map[Class, Set[Class]]())((m : Map[Class, Set[Class]], cd: ClassDefinition) =>
        cd.superclass.cata(sup => m.adjust(sup)(_ + Class(cd.name) + Class("Nothing")), m)
      ).trans
    } + (Class("Any") -> defs.keys.toSet)

    val subtypesOrSelf: Map[Class, Set[Class]] =
      subtypes.map(((c : Class, sts : Set[Class]) => (c, sts + c)).tupled)

    {
      val commoncr = childfields intersect reffields
      assert(commoncr.isEmpty, s"There are overlapping names used for fields and references: ${commoncr.mkString(", ")}")
    }
  }
}
