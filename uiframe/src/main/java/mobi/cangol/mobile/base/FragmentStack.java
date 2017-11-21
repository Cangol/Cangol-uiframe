package mobi.cangol.mobile.base;

import java.lang.ref.WeakReference;
import java.util.Stack;

/**
 * Created by xuewu.wei on 2017/10/26.
 */

public class FragmentStack {
    Stack<WeakReference<BaseFragment>> stack = null;
    Stack<String> tagStack = null;

    public FragmentStack() {
        stack = new Stack<>();
        tagStack = new Stack<>();
    }

    public int size() {
        return stack.size();
    }

    public void addFragment(BaseFragment fragment) {
        stack.add(new WeakReference<>(fragment));
    }

    public void addTag(String tag) {
        tagStack.add(tag);
    }

    public BaseFragment peekFragment() {
        return stack.peek().get();
    }

    public String peekTag() {
        return tagStack.peek();
    }

    public BaseFragment popFragment() {
        return stack.pop().get();
    }

    public String popTag() {
        return tagStack.pop();
    }

    public boolean containsTag(String tag){
        return tagStack.contains(tag);
    }

    public Stack<String> getTag() {
        return tagStack;
    }
}
