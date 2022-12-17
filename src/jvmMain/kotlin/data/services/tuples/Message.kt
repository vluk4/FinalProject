package data.services.tuples

import net.jini.core.entry.Entry

class Message : Entry {
    @JvmField
    var nome: String? = null
    @JvmField
    var mensagem: String? = null
}