package com.lifeistech.android.zinrou;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final int MURABITO_NUMBER = 5;

    Random random = new Random();

    int turn;

    boolean[] murabitos = new boolean[MURABITO_NUMBER];
    boolean[] sinda = new boolean[MURABITO_NUMBER];

    List<Integer> selectedMurabitos = new ArrayList<>();

    /**
     * アプリが起動した時に呼び出される
     * 人狼ゲームを開始する
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 人狼三人選ぶ
        for (int i = 0; i < 2; i++) {
            int index = random.nextInt(MURABITO_NUMBER);

            // すでに人狼だった場合
            while (murabitos[index]) {
                index = random.nextInt(MURABITO_NUMBER);
            }

            //　ランダムで人狼にする
            murabitos[index] = true;
        }
        turn = -1;
    }

    public void start(View v) {
        setContentView(R.layout.layout_yoru);
    }

    /**
     * 夜のターンの確認画面を表示する
     * <p/>
     * もし全員回ったら、朝の結果画面を表示する
     *
     * @param v
     */
    public void confirm(View v) {
        turn = turn + 1;

        while (turn < MURABITO_NUMBER && sinda[turn]) {
            turn = turn + 1;
        }

        if (turn >= MURABITO_NUMBER) {
            turn = -1;
            setContentView(R.layout.layout_asa);

            // 誰を殺すかを決定する
            int index = selectedMurabitos.get(random.nextInt(selectedMurabitos.size()));
            // リストの中身を空にする
            selectedMurabitos.clear();

            sinda[index] = true;

            // 誰が殺されたかを表示する
            TextView text = (TextView) findViewById(R.id.textView4);
            text.setText((index + 1) + "番目");

            return;
        }

        setContentView(R.layout.layout_confirm_number);
        TextView number = (TextView) findViewById(R.id.number);
        number.setText((turn + 1) + "番目");
    }

    /**
     * 次のひとの画面を表示する
     *
     * @param v
     */
    public void next(View v) {

        if (murabitos[turn]) {
            setContentView(R.layout.layout_jinro);
        } else {
            setContentView(R.layout.layout_murabito);
        }
    }

    /**
     * 人狼が食べる人を選ぶボタンを押した時の処理を書く
     * <p/>
     * ダイアログで誰を食べるかを選択する
     *
     * @param v
     */
    public void jinro(View v) {

        final List<String> items = new ArrayList<>();

        for (int i = 0; i < MURABITO_NUMBER; i++) {
            if(!sinda[i]){
                items.add((i + 1) + "");
            }
        }

        String[] itemsArray = new String[items.size()];

        new AlertDialog.Builder(this)
                .setTitle("誰を食べますか？")
                .setSingleChoiceItems(items.toArray(itemsArray), 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ダイアログを閉じる
                        dialog.dismiss();

                        // タップされた数字(文字)を取り出して、数字に変換
                        String numString = items.get(which);
                        int index = Integer.parseInt(numString) - 1;

                        // 選択された人として追加する
                        selectedMurabitos.add(index);

                        setContentView(R.layout.layout_jinro_selected);
                    }
                }).show();
    }

    /**
     * 朝を迎えた時の画面を表示する
     *
     * @param v
     */
    public void asa(View v) {
        setContentView(R.layout.layout_asa_choose);
    }

    /**
     * 処刑する人を選ぶボタンを押した時の処理をする
     * <p/>
     * ダイアログを表示して処刑する人を選ぶ
     *
     * @param v
     */
    public void choose(View v) {

        final List<String> items = new ArrayList<>();
        for (int i = 0; i < MURABITO_NUMBER; i++) {
            if (!sinda[i]) {
                items.add((i + 1) + "");
            }
        }

        String[] itemsArray = new String[items.size()];

        new AlertDialog.Builder(this)
                .setTitle("誰を殺しますか？")
                .setSingleChoiceItems(items.toArray(itemsArray), 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ダイアログを閉じる
                        dialog.dismiss();

                        // タップされた数字(文字)を取り出して、数字に変換
                        String numString = items.get(which);
                        int index = Integer.parseInt(numString) - 1;

                        sinda[index] = true;

                        setContentView(R.layout.layout_asa_selected);
                        TextView textView = (TextView) findViewById(R.id.textView);
                        textView.setText((index + 1) + "番目");
                    }
                })
                .show();
    }


    /**
     *
     * 人狼の方が人数が多くなった場合最後の画面を表示する
     *
     */

}
