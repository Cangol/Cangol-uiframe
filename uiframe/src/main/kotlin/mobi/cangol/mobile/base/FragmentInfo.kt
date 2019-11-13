package mobi.cangol.mobile.base

import android.os.Bundle

class FragmentInfo {
    val tag: String?
    val clazz: Class<out BaseFragment>
    val args: Bundle?

    constructor(clazz: Class<out BaseFragment>, tag: String, args: Bundle?) {
        this.tag = tag
        this.clazz = clazz
        this.args = args
    }

    constructor(fragment: BaseFragment) {
        tag = fragment.tag
        clazz = fragment.javaClass
        args = fragment.arguments
    }

    override fun toString(): String {
        return "FragmentInfo [tag=$tag, clazz=$clazz, args=$args]"
    }

}
