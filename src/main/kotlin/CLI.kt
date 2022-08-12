import nl.bigo.prce.PCREBaseListener
import nl.bigo.prce.PCRELexer
import nl.bigo.prce.PCREParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker

object CLI {
    @JvmStatic
    fun main(argv: Array<String>) {
        val stringStream = CharStreams.fromString("([A-Z]{2})-?([А-Я]{2}) ?№? ?(\\d{6})")
        val lexer = PCRELexer(stringStream)
        val parser = PCREParser(CommonTokenStream(lexer))

        val parseContext = parser.parse()
        val walker = ParseTreeWalker()
        val listener = TranslatorListener()
        walker.walk(listener, parseContext)
        println("Done. ${listener.toString()}")
    }
}

class TranslatorListener: PCREBaseListener() {
    private val stringBuilder = StringBuilder()
    var currentGroupIndex = 1

    override fun enterCapture(ctx: PCREParser.CaptureContext) {
        super.enterCapture(ctx)
        stringBuilder.append("\$$currentGroupIndex")
        currentGroupIndex++
    }

    override fun enterLiteral(ctx: PCREParser.LiteralContext) {
        super.enterLiteral(ctx)

        stringBuilder.append(ctx.shared_literal().text)
    }

    override fun toString() = stringBuilder.toString()
}