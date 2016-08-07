/**
 * Copyright (c) 2013 Cangol
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mobi.cangol.mobile.base;

import android.os.Bundle;

public class FragmentInfo {
    final String tag;
    final Class<? extends BaseFragment> clss;
    final Bundle args;

    public FragmentInfo(Class<? extends BaseFragment> _class, String _tag, Bundle _args) {
        tag = _tag;
        clss = _class;
        args = _args;
    }

    public FragmentInfo(BaseFragment fragment) {
        tag = fragment.getTag();
        clss = fragment.getClass();
        args = fragment.getArguments();
    }

    @Override
    public String toString() {
        return "FragmentInfo [tag=" + tag + ", clss=" + clss + ", args=" + args
                + "]";
    }

}
