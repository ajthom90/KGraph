package it.lamba.kgraph.data.impl

import it.lamba.kgraph.data.Edge
import it.lamba.kgraph.searches.Frontier
import it.lamba.kgraph.data.Node
import it.lamba.utils.forEach

class KFrontier(override val nextElementEvaluator: (Frontier.Element) -> Double):
    Frontier {

    private val data = HashMap<Node, Frontier.Element>()

    override fun next(): Frontier.Element {
        var bestNode: Node? = null
        forEach { node, element ->
            if(bestNode == null || nextElementEvaluator(element) > nextElementEvaluator(this[bestNode!!]!!))
                bestNode = node
        }
        return remove(bestNode)!!
    }

    override fun hasNext() = isNotEmpty()

    override val entries: MutableSet<MutableMap.MutableEntry<Node, Frontier.Element>>
        get() = data.entries
    override val keys: MutableSet<Node>
        get() = data.keys
    override val size: Int
        get() = data.size
    override val values: MutableCollection<Frontier.Element>
        get() = data.values

    override fun clear() = data.clear()

    override fun containsKey(key: Node) = data.containsKey(key)

    override fun containsValue(value: Frontier.Element) = data.containsValue(value)

    override fun get(key: Node) = data[key]

    override fun isEmpty() = data.isEmpty()

    override fun put(key: Node, value: Frontier.Element): Frontier.Element? {
        val previous = data[key]
        if(previous == null || value.costUntilHere < previous.costUntilHere)
            data[key] = value
        return previous
    }

    override fun putAll(from: Map<out Node, Frontier.Element>)
            = from.forEach { put(it.key, it.value)}

    override fun remove(key: Node) = data.remove(key)

    data class KElement(
        override val node: Node,
        override val depth: Int,
        override val path: ArrayList<Edge>,
        override val costUntilHere: Double,
        override val parentNode: Node? = null,
        override val heuristic: Double? = 0.0
    ) : Frontier.Element

}
