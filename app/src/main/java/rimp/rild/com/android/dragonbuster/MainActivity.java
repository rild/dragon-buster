package rimp.rild.com.android.dragonbuster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    final int DRAGON_HP = 9999;
    int dragonHp;

    TextView dragonHpText;
    ImageView enemyEffectImage;
    ImageView dragonImage;
    ImageView playerImage;

    TextView gameMessageText;

    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pokemon_gba_battle);

        random = new Random();

//        getViews();
//        reset();
//        updateViews();

    }

    private void reset() {
        dragonHp = DRAGON_HP;
        dragonImage.setVisibility(View.VISIBLE);
        enemyEffectImage.setVisibility(View.INVISIBLE);
        gameMessageText.setText("Red Dragon emerged!");
        setDefautAnimation();
    }

    private void getViews() {
        dragonHpText = (TextView) findViewById(R.id.enemy_hp_value);
        enemyEffectImage = (ImageView) findViewById(R.id.enemy_effect);
        dragonImage = (ImageView) findViewById(R.id.imageView_dragon);
        playerImage = (ImageView) findViewById(R.id.imageView_player);
        gameMessageText = (TextView) findViewById(R.id.game_message);
    }

    private void updateViews() {
        dragonHpText.setText(String.valueOf(dragonHp));
    }

    private void fire() {
        firePlayerAnimation();
        fireEffectAnimation();

        damageDragon(getRandomDamage(1000, 100));
        updateViews();
    }

    private void firePlayerAnimation() {
        AnimationSet set = new AnimationSet(true);

        float startPointX = dragonImage.getX();
        float startPointY = dragonImage.getY();

        TranslateAnimation translate =
                new TranslateAnimation(startPointX, startPointX + 5, startPointY, startPointY - 40);
        translate.setRepeatMode(Animation.REVERSE);
        translate.setRepeatCount(1);
        translate.setDuration(150);
        set.addAnimation(translate);

        playerImage.startAnimation(set);
    }

    private void fireEffectAnimation() {
        AnimationSet set = new AnimationSet(true);

        float startPointX = enemyEffectImage.getX();
        float startPointY = enemyEffectImage.getY();

        TranslateAnimation translate =
                new TranslateAnimation(startPointX, startPointX + 20, startPointY, startPointY + 5);
        translate.setDuration(100);
        translate.setRepeatCount(5);
        translate.setRepeatMode(Animation.REVERSE);
        set.addAnimation(translate);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0.9f, 0.89f, 0.9f, 0.88f);
        scaleAnimation.setRepeatCount(5);
        scaleAnimation.setDuration(50);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        set.addAnimation(scaleAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.01f);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                enemyEffectImage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                enemyEffectImage.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        alphaAnimation.setDuration(1000);
        set.addAnimation(alphaAnimation);

        enemyEffectImage.startAnimation(set);
    }

    private void attack() {
        AnimationSet set = new AnimationSet(true);

        //なぜかplayerImageにすると画面外に飛び出ていく
        float startPointX = dragonImage.getX();
        float startPointY = dragonImage.getY();

        TranslateAnimation playerMovement = new TranslateAnimation(startPointX, startPointX - 100, startPointY, startPointY - 30);
        playerMovement.setDuration(80);
        set.addAnimation(playerMovement);

        playerImage.startAnimation(set);

        int damage = getRandomDamage(100, 100);
        damageDragon(damage);
        updateViews();
    }

    private int getRandomDamage(int baseDamage, int maxJitter) {
        int damage = 0;

        int flag = random.nextInt(2);
        int jitter = random.nextInt(maxJitter);

        if (flag % 2 == 0) {
            damage = baseDamage + jitter;
        } else {
            damage = baseDamage - jitter;
        }
        return damage;
    }

    private void damageDragon(int damage) {
        dragonHp -= damage;

        damageEffectAnimation();
        gameMessageText.setText("You hit for " + damage + " points!");


        if (dragonHp <= 0) {
            gameMessageText.setText("Congratulations!");
            clearDragon();
        }
    }

    private void damageEffectAnimation() {
        AnimationSet set = new AnimationSet(true);

        TranslateAnimation vibrateAnimation = vibrateDragon(30, 5);
        vibrateAnimation.setDuration(50);
        vibrateAnimation.setRepeatCount(5);
        set.addAnimation(vibrateAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.8f);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        alphaAnimation.setDuration(50);
        alphaAnimation.setRepeatCount(5);
        set.addAnimation(alphaAnimation);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setDefautAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        dragonImage.startAnimation(set);
    }

    private TranslateAnimation vibrateDragon(float jitterX, float jitterY) {
        float startPointX = dragonImage.getX();
        float startPointY = dragonImage.getY();

        TranslateAnimation translate =
                new TranslateAnimation(startPointX, startPointX + jitterX, startPointY, startPointY + jitterY);
        return translate;
    }

    private void clearDragon() {
        dragonHp = 0;
        updateViews();
        AnimationSet set = new AnimationSet(true);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(1500);
        set.addAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dragonImage.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        dragonImage.startAnimation(set);
    }

    private void setDefautAnimation() {
        AnimationSet set = new AnimationSet(true);

        TranslateAnimation vibrateAnimation = vibrateDragon(5, 0);
        vibrateAnimation.setDuration(1000);
        vibrateAnimation.setRepeatMode(Animation.REVERSE);
        vibrateAnimation.setRepeatCount(Animation.INFINITE);
        set.addAnimation(vibrateAnimation);

        dragonImage.startAnimation(set);

        playerImage.startAnimation(set);
    }

    public void attack(View v) {
        attack();
    }

    public void fire(View v) {
        fire();
    }

    public void restart(View v) {
        reset();
        updateViews();
    }
}
