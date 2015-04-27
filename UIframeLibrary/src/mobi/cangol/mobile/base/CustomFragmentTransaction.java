package mobi.cangol.mobile.base;

import android.support.v4.app.FragmentTransaction;


public class CustomFragmentTransaction {
	private BaseFragment mSource;
	private int mRequestCode;
	private int mEnterAnimation;
	private int mExitAnimation;
	private int mPopStackEnterAnimation;
	private int mPopStackExitAnimation;
	
	public CustomFragmentTransaction setTargetFragment(BaseFragment source,int requestCode){
		this.mSource=source;
		this.mRequestCode=requestCode;
		return this;
	}
	
	public  CustomFragmentTransaction setCustomAnimations(int enter, int exit){
		this.mEnterAnimation = enter;
		this.mExitAnimation = exit;
		return this;
	}
	public  CustomFragmentTransaction setCustomAnimations(int enter, int exit,int popEnter, int popExit){
		this.mEnterAnimation = enter;
		this.mExitAnimation = exit;
		this.mPopStackEnterAnimation = popEnter;
		this.mPopStackExitAnimation = popExit;
		return this;
	}

	public void fillTargetFragment(BaseFragment target){
		if(mSource!=null&&mRequestCode!=-1)
		target.setTargetFragment(mSource, mRequestCode);
	}
	
	
	public boolean fillCustomAnimations(FragmentTransaction fragmentTransaction){
		if(mEnterAnimation>0&&mExitAnimation>0&&mPopStackEnterAnimation>0&&mPopStackExitAnimation>0){
			fragmentTransaction.setCustomAnimations(mEnterAnimation,mExitAnimation,mPopStackEnterAnimation,mPopStackExitAnimation);
			return true;
		}else if(mEnterAnimation>0&&mExitAnimation>0){
			fragmentTransaction.setCustomAnimations(mEnterAnimation,mExitAnimation);
			return true;
		}else{
			return false;
		}
	}
}
