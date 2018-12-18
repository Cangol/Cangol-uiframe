package mobi.cangol.mobile.base;


import android.text.TextUtils;

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
        return stack.isEmpty()?null:stack.peek().get();
    }

    public String peekTag() {
        return tagStack.isEmpty()?null:tagStack.peek();
    }

    public BaseFragment popFragment() {

        return stack.isEmpty()?null:stack.pop().get();
    }

    public void popFragment(String tag,int flag) {
        if(TextUtils.isEmpty(tag)){
            if(flag==1){
                while (stack.size() > 1) {
                    stack.pop();
                    tagStack.pop();
                }
            }else{
                stack.pop();
                tagStack.pop();
            }
        }else{
            int index=tagStack.indexOf(tag);
            if(index>0){
                while (!tag.equals(tagStack.peek())) {
                    tagStack.pop();
                    stack.pop();
                }
                if(flag==1){
                    tagStack.pop();
                    stack.pop();
                }
            }
        }
    }

    public String popTag() {
        return tagStack.isEmpty()?null:tagStack.pop();
    }

    public boolean containsTag(String tag){
        return tagStack.contains(tag);
    }

    public Stack<String> getTag() {
        return tagStack;
    }

    public void clear() {
        stack.clear();
        tagStack.clear();
    }
}
