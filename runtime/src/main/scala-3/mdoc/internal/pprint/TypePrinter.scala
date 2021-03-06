package mdoc.internal.pprint

import scala.language.implicitConversions
import scala.quoted._
import scala.quoted.show.SyntaxHighlight

trait TPrint[T]{
  def render: String
}

object TPrint {
  inline given default[T] as TPrint[T] = ${ TypePrinter.typeString[T] }
}

object TypePrinter{

  def typeString[T](using ctx: QuoteContext, tpe: Type[T]): Expr[TPrint[T]] = {
    import ctx.tasty._

    val typePrinter = new SourceTypePrinter(ctx.tasty)(SyntaxHighlight.plain)
    
    '{  new TPrint[T]{ def render: String = ${ Expr(typePrinter.showTypeOrBounds(tpe.unseal.tpe)) } }  }
  }
}
