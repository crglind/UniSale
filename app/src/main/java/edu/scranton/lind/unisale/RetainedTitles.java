package edu.scranton.lind.unisale;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.Stack;

public class RetainedTitles extends Fragment {

    private Stack<String> TitlesStack;
    private String CurrentTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setTitlesStack(Stack<String> stack){TitlesStack = (Stack<String>)stack.clone();}

    public void setCurrentTitle(String title) {CurrentTitle = title;}

    public Stack getTitlesStack(){return TitlesStack;}

    public String getCurrentTitle() {return CurrentTitle;}
}
