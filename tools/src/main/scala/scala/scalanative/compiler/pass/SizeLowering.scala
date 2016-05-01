package scala.scalanative
package compiler
package pass

import scala.collection.mutable
import util.ScopedVar, ScopedVar.scoped
import nir._

/** Eliminates:
  * - Type.Size
  * - Op.{SizeOf, ArrSizeOf}
  */
class SizeLowering(implicit fresh: Fresh) extends Pass {
  override def preInst = {
    case Inst(n, Op.Sizeof(ty)) =>
      val elem = Val.Local(fresh(), Type.Ptr)
      Seq(
          Inst(elem.name, Op.Elem(ty, Val.Null, Seq(Val.I32(1)))),
          Inst(n, Op.Conv(Conv.Ptrtoint, Type.I64, elem))
      )
  }

  override def preType = {
    case Type.Size => Type.I64
  }
}