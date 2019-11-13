package mobi.cangol.mobile.base

import android.text.TextUtils
import java.util.*


/**
 * Created by xuewu.wei on 2017/10/26.
 */

class FragmentStack {
    private val stack: Stack<BaseFragment> = Stack()
    private val tag: Stack<String> = Stack()

    fun size(): Int {
        return stack.size
    }

    fun getTags(): Stack<String> {
        return tag
    }

    fun addFragment(fragment: BaseFragment) {
        stack.add(fragment)
    }

    fun addTag(tag: String) {
        this.tag.add(tag)
    }

    fun peekFragment(): BaseFragment? {
        return if (stack.isEmpty()) null else stack.peek()
    }

    fun peekTag(): String? {
        return if (tag.isEmpty()) null else tag.peek()
    }

    fun popFragment(): BaseFragment? {
        return if (stack.isEmpty()) null else stack.pop()
    }

    fun popFragment(tag: String, flag: Int) {
        if (TextUtils.isEmpty(tag)) {
            if (flag == 1) {
                while (stack.size > 1) {
                    stack.pop()
                    this.tag.pop()
                }
            } else {
                stack.pop()
                this.tag.pop()
            }
        } else {
            val index = this.tag.indexOf(tag)
            if (index > 0) {
                while (tag != this.tag.peek()) {
                    this.tag.pop()
                    stack.pop()
                }
                if (flag == 1) {
                    this.tag.pop()
                    stack.pop()
                }
            }
        }
    }

    fun popTag(): String? {
        return if (tag.isEmpty()) null else tag.pop()
    }

    fun containsTag(tag: String): Boolean {
        return this.tag.contains(tag)
    }

    fun clear() {
        stack.clear()
        tag.clear()
    }
}
