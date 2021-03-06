package ca.georgebrown.mobilegamesassignment1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import org.xml.sax.Attributes;
import android.util.FloatMath;

import java.io.IOException;
import java.util.Random;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
public class MainActivity extends AppCompatActivity {
    Canvas canvas;

    SlotMachineView SlotMachineView;
//Hi
    /*
    //Sound
    //initialize sound variables
    private SoundPool soundPool;
    int sample1 = -1;
    int sample2 = -1;
    int sample3 = -1;
    int sample4 = -1;

    */

    //Used for getting display details like the number of pixels
    Display display;
    Point size;
    int screenWidth;
    int screenHeight;



    // Slot Machine Variables:
    int playerMoney = 100;
    int winnings = 0;
    int jackpot = 5000;
    int turn = 0;
    int playerBet = 10;
    int winNumber = 0;
    int lossNumber = 0;
    int spinResult;
    //char fruits = ""; //String?
    int winRatio = 0;
    int defaultMoney = 100;

    //types of reels:
    int xwing = 0;
    int atat = 0;
    int blasters = 0;
    int lightsaber = 0;
    int vader = 0;
    int c3po = 0;
    int r2d2 = 0;
    int stormtroopers = 0;

    //Reels:
    //Top
    int spin11 =0; int spin12 =0; int spin13 =0;
    //Middle
    int spin21 =0; int spin22 =0; int spin23 =0;
    //Bottom
    int spin31 =0; int spin32 =0; int spin33 =0;

    //stats
    long lastFrameTime;
    int fps;

    private TextView msg;
    private TextView moneyValue;
    private TextView betValue;
    private ImageView img1, img2, img3, img4, img5, img6, img7, img8, img9;
    private Wheel wheel11, wheel12, wheel13, wheel21, wheel22, wheel23, wheel31, wheel32, wheel33;
    private Button btn;
    private Button btnBet10;
    private Button btnBet50;
    private Button btnBet100;
    private Button reset;
    private boolean isStarted;

    public static final Random RANDOM = new Random();

    public static long randomLong(long lower, long upper) {
        return lower + (long) (RANDOM.nextDouble() * (upper - lower));
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SlotMachineView = new SlotMachineView(this);
       // setContentView(SlotMachineView);
        setContentView(R.layout.activity_main);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        img5 = (ImageView) findViewById(R.id.img5);
        img6 = (ImageView) findViewById(R.id.img6);
        img7 = (ImageView) findViewById(R.id.img7);
        img8 = (ImageView) findViewById(R.id.img8);
        img9 = (ImageView) findViewById(R.id.img9);

        btn = (Button) findViewById(R.id.Spin);
        btnBet10 = (Button) findViewById(R.id.BetButton);
        btnBet50 = (Button) findViewById(R.id.button);
        btnBet100 = (Button) findViewById(R.id.button3);
        reset = (Button) findViewById(R.id.button2);
        msg = (TextView) findViewById(R.id.msg);
        moneyValue = (TextView) findViewById(R.id.MoneyAmount);
        moneyValue.setText(String.valueOf(playerMoney));

        betValue = (TextView) findViewById(R.id.textView5);
        betValue.setText(String.valueOf(playerBet));

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerBet = 10;
                playerMoney = defaultMoney;
                moneyValue.setText(String.valueOf(playerMoney));
                betValue.setText(String.valueOf(playerBet));
                msg.setText("Game Reset");
            }
            });
        btnBet10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerMoney >= 10) {
                    playerBet = 10;
                    msg.setText("You bet 10");
                    betValue.setText(String.valueOf(playerBet));
                }
                else
                    msg.setText("You don't have enough money!");
            }
                               });

        btnBet50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerMoney >= 50) {
                    playerBet = 50;
                    msg.setText("You bet 50");
                    betValue.setText(String.valueOf(playerBet));
                }
                else
                msg.setText("You don't have enough money!");
            }
        });

        btnBet100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerMoney >= 100) {
                    playerBet = 100;
                    msg.setText("You bet 100");
                    betValue.setText(String.valueOf(playerBet));
                }
                else
                msg.setText("You don't have enough money!");
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
        @Override
                public void onClick(View view) {
            if (playerMoney > 0) {
                if (isStarted) {
                    wheel11.stopWheel();
                    wheel12.stopWheel();
                    wheel13.stopWheel();
                    wheel21.stopWheel();
                    wheel22.stopWheel();
                    wheel23.stopWheel();
                    wheel31.stopWheel();
                    wheel32.stopWheel();
                    wheel33.stopWheel();

                    if ((wheel11.currentIndex == wheel12.currentIndex && wheel12.currentIndex == wheel13.currentIndex) || (wheel21.currentIndex == wheel22.currentIndex &&
                            wheel22.currentIndex == wheel23.currentIndex) || (wheel31.currentIndex == wheel32.currentIndex && wheel32.currentIndex == wheel33.currentIndex)) {
                        msg.setText("You won big, collect your bet!");
                        playerMoney += playerBet * 100;
                        moneyValue.setText(String.valueOf(playerMoney));
                    } else if ((wheel11.currentIndex == wheel12.currentIndex) || (wheel21.currentIndex == wheel22.currentIndex) ||
                            (wheel31.currentIndex == wheel32.currentIndex)) {
                        msg.setText("You won small prize");
                        playerMoney += playerBet * 10;
                        moneyValue.setText(String.valueOf(playerMoney));
                    } else {
                        msg.setText("You lose");
                        playerMoney -= playerBet;
                        moneyValue.setText(String.valueOf(playerMoney));
                    }
                    btn.setText("Spin");
                    isStarted = false;

                } else {
                    wheel11 = new Wheel(new Wheel.WheelListener() {
                        @Override
                        public void newImage(final int img) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img1.setImageResource(img);
                                }
                            });
                        }
                    }, 20, randomLong(0, 200));
                    wheel11.start();

                    wheel12 = new Wheel(new Wheel.WheelListener() {
                        @Override
                        public void newImage(final int img) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img2.setImageResource(img);
                                }
                            });
                        }
                    }, 20, randomLong(150, 400));
                    wheel12.start();

                    wheel13 = new Wheel(new Wheel.WheelListener() {
                        @Override
                        public void newImage(final int img) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img3.setImageResource(img);
                                }
                            });
                        }
                    }, 20, randomLong(150, 400));
                    wheel13.start();

                    wheel21 = new Wheel(new Wheel.WheelListener() {
                        @Override
                        public void newImage(final int img) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img4.setImageResource(img);
                                }
                            });
                        }
                    }, 20, randomLong(150, 400));
                    wheel21.start();

                    wheel22 = new Wheel(new Wheel.WheelListener() {
                        @Override
                        public void newImage(final int img) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img5.setImageResource(img);
                                }
                            });
                        }
                    }, 20, randomLong(150, 400));
                    wheel22.start();

                    wheel23 = new Wheel(new Wheel.WheelListener() {
                        @Override
                        public void newImage(final int img) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img6.setImageResource(img);
                                }
                            });
                        }
                    }, 20, randomLong(150, 400));
                    wheel23.start();

                    wheel31 = new Wheel(new Wheel.WheelListener() {
                        @Override
                        public void newImage(final int img) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img7.setImageResource(img);
                                }
                            });
                        }
                    }, 20, randomLong(150, 400));
                    wheel31.start();

                    wheel32 = new Wheel(new Wheel.WheelListener() {
                        @Override
                        public void newImage(final int img) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img8.setImageResource(img);
                                }
                            });
                        }
                    }, 20, randomLong(150, 400));
                    wheel32.start();

                    wheel33 = new Wheel(new Wheel.WheelListener() {
                        @Override
                        public void newImage(final int img) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img9.setImageResource(img);
                                }
                            });
                        }
                    }, 20, randomLong(150, 400));
                    wheel33.start();

                    btn.setText("Stop");
                    msg.setText("");
                    isStarted = true;
                }
            }
            else
            {
                msg.setText("You ran out of Money!!!");
            }
            }
        });





        //Sound code
        /*soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        try {
            //Create objects of the 2 required classes
            AssetManager assetManager = getAssets();
            AssetFileDescriptor descriptor;

            //create our three fx in memory ready for use
            descriptor = assetManager.openFd("sample1.ogg");
            sample1 = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("sample2.ogg");
            sample2 = soundPool.load(descriptor, 0);


            descriptor = assetManager.openFd("sample3.ogg");
            sample3 = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("sample4.ogg");
            sample4 = soundPool.load(descriptor, 0);


        } catch (IOException e) {
            //catch exceptions here
        }
        */

        //Get the screen size in pixels
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

    class SlotMachineView extends SurfaceView implements Runnable {
        Thread ourThread = null;
        SurfaceHolder ourHolder;
        volatile boolean playingSlots;
        Paint paint;

        public SlotMachineView(Context context) {
            super(context);
            ourHolder = getHolder();
            paint = new Paint();
        }

        @Override
        public void run() {
            while (playingSlots) {
                updateCourt();
                drawCourt();
                controlFPS();
            }
        }


        //Slot class:

        public class PlaySlotGame{

            int value = 0; //assuming these are ints during int
            int lowerBounds = 0; //assuming these are ints during int
            int upperBounds = 0; //assuming these are ints during int
            String message;

            public void myDrawText(String message, int textSize, int Red, int Green, int Blue, int X, int Y){

                paint.setColor(Color.argb(255, Red, Green, Blue));
                paint.setTextSize(textSize);
                canvas.drawText(message, X, Y, paint);
            }




            // Utility function to show Player Stats

            public void showPlayerStats(){
                winRatio = winNumber / turn;
                /*
                $("#jackpot").text("Jackpot: " + jackpot);
                $("#playerMoney").text("Player Money: " + playerMoney);
                $("#playerTurn").text("Turn: " + turn);
                $("#playerWins").text("Wins: " + winNumber);
                $("#playerLosses").text("Losses: " + lossNumber);
                $("#playerWinRatio").text("Win Ratio: " + (winRatio * 100).toFixed(2) + "%");
                */
            }


// Utility function to reset all fruit tallies

            void resetFruitTally() {
                xwing = 0;
                atat = 0;
                blasters = 0;
                lightsaber = 0;
                vader = 0;  //bars
                c3po = 0;
                r2d2 = 0;
                stormtroopers = 0;  //blanks
            }

// Utility function to reset the player stats
            void resetAll() {
                playerMoney = 1000;
                winnings = 0;
                jackpot = 5000;
                turn = 0;
                playerBet = 0;
                winNumber = 0;
                lossNumber = 0;
                winRatio = 0;
            }


// Check to see if the player won the jackpot
            void checkJackPot() {
    // compare two random values
                double jackPotTry = Math.floor(Math.random() * 51 + 1);
                double jackPotWin = Math.floor(Math.random() * 51 + 1);
                if (jackPotTry == jackPotWin) {
                    paint.setColor(Color.argb(255, 255, 255, 255));//white
                    paint.setTextSize(45);
                    canvas.drawText("You Won the $" + jackpot + " Jackpot!!", 20, 500, paint);
                    //alert("You Won the $" + jackpot + " Jackpot!!"); //Call to paint!
                    playerMoney += jackpot;
                    jackpot = 1000;
                }
            }


// Utility function to show a win message and increase player money
            void showWinMessage() {
                playerMoney += winnings;
                //$("div#winOrLose>p").text("You Won: $" + winnings); //Call to paint!
                resetFruitTally();
                checkJackPot();
            }


// Utility function to show a loss message and reduce player money
            void showLossMessage() {
                playerMoney -= playerBet;
                //$("div#winOrLose>p").text("You Lost!"); //Call to paint!
                resetFruitTally();
            }


// Utility function to check if a value falls within a range of bounds
            boolean checkRange(int value, int lowerBounds, int upperBounds) { //assuming these are ints during init
                if (value >= lowerBounds && value <= upperBounds)
                {
                    return true;
                }
                else {

                    return false;
                }
            }

// Spinning the reels
            void Reels() {

                //Start the randomization process:
                //Top
                spin11 = (int)Math.floor((Math.random() * 65) + 1);
                spin12 = (int)Math.floor((Math.random() * 65) + 1);
                spin13 = (int)Math.floor((Math.random() * 65) + 1);
                //Middle
                spin21 = (int)Math.floor((Math.random() * 65) + 1);
                spin22 = (int)Math.floor((Math.random() * 65) + 1);
                spin23 = (int)Math.floor((Math.random() * 65) + 1);
                //Bottom?
                spin31 = (int)Math.floor((Math.random() * 65) + 1);
                spin32 = (int)Math.floor((Math.random() * 65) + 1);
                spin33 = (int)Math.floor((Math.random() * 65) + 1);

                int[] reelList = new int[]{
                        spin11,
                        spin12,
                        spin13,
                        spin21,
                        spin22,
                        spin23,
                        spin31,
                        spin32,
                        spin33,
                };

                //Below is only half done.  Need to test code before I finish.
                for (int index = 0; index < reelList.length; index++) {
                    if (index >= 1 && index <= 27) {
                        xwing++;
                    } else if (reelList[index] >= 28 && reelList[index] <= 37) {
                        atat++;
                    } else if (reelList[index] >= 38 && reelList[index] <= 46) {
                        blasters++;
                    } else if (reelList[index] >= 47 && reelList[index] <= 54) {
                        vader++;
                    } else if (reelList[index] >= 55 && reelList[index] <= 59) {
                        c3po++;
                    } else if (reelList[index] >= 60 && reelList[index] <= 62) {
                        stormtroopers++;
                    } else if (reelList[index] >= 63 && reelList[index] <= 64) {
                        lightsaber++;
                    } else if (reelList[index] >= 65 && reelList[index] <= 65) {
                        r2d2++;
                    }
                }
            }

// This function calculates the player's winnings, if any
            void determineWinnings()
            {

                /*
                xwing = 0;
                atat = 0;
                blasters = 0;
                vader = 0;
                c3po = 0;
                stormtroopers = 0;
                lightsaber = 0;
                r2d2 = 0;

                if (blanks == 0)
                {
                    if (xwing == 3) {
                        winnings = playerBet * 10;
                    }
                    else if(atat == 3) {
                        winnings = playerBet * 20;
                    }
                    else if (blasters == 3) {
                        winnings = playerBet * 30;
                    }
                    else if (lightsaber == 3) {
                        winnings = playerBet * 40;
                    }
                    else if (vader == 3) {
                        winnings = playerBet * 50;
                    }
                    else if (c3po == 3) {
                        winnings = playerBet * 75;
                    }
                    else if (r2d2 == 3) {
                        winnings = playerBet * 100;
                    }
                    else if (xwing == 2) {
                        winnings = playerBet * 2;
                    }
                    else if (atat == 2) {
                        winnings = playerBet * 2;
                    }
                    else if (blasters == 2) {
                        winnings = playerBet * 3;
                    }
                    else if (lightsaber == 2) {
                        winnings = playerBet * 4;
                    }
                    else if (vader == 2) {
                        winnings = playerBet * 5;
                    }
                    else if (c3po == 2) {
                        winnings = playerBet * 10;
                    }
                    else if (r2d2 == 2) {
                        winnings = playerBet * 20;
                    }
                    else if (r2d2 == 1) {
                        winnings = playerBet * 5;
                    }
                    else {
                        winnings = playerBet * 1;
                    }
                    winNumber++;
                    showWinMessage();
                }
                else
                {
                    lossNumber++;
                    showLossMessage();
                }
*/
            }
            /*
// When the player clicks the spin button the game kicks off
            $("#spinButton").click(function () {
                playerBet = $("div#betEntry>input").val();

                if (playerMoney == 0)
                {
                    if (confirm("You ran out of Money! \nDo you want to play again?")) {
                        resetAll();
                        showPlayerStats();
                    }
                }
                else if (playerBet > playerMoney) {
                    alert("You don't have enough Money to place that bet.");
                }
                else if (playerBet < 0) {
                    alert("All bets must be a positive $ amount.");
                }
                else if (playerBet <= playerMoney) {
                    spinResult = Reels();
                    fruits = spinResult[0] + " - " + spinResult[1] + " - " + spinResult[2];
                    $("div#result>p").text(fruits);
                    determineWinnings();
                    turn++;
                    showPlayerStats();
                }
                else {
                    alert("Please enter a valid bet amount");
                }

            });
             */
        }



        public void updateCourt() {
            /*
            if (racketIsMovingRight) {
                racketPosition.x = racketPosition.x + 10;
            }

            if (racketIsMovingLeft) {
                racketPosition.x = racketPosition.x - 10;
            }


            //detect collisions

            //hit right of screen
            if (ballPosition.x + ballWidth > screenWidth) {
                ballIsMovingLeft = true;
                ballIsMovingRight = false;
                //soundPool.play(sample1, 1, 1, 0, 0, 1);
            }

            //hit left of screen
            if (ballPosition.x < 0) {
                ballIsMovingLeft = false;
                ballIsMovingRight = true;
                //soundPool.play(sample1, 1, 1, 0, 0, 1);
            }

            //Edge of ball has hit bottom of screen
            if (ballPosition.y > screenHeight - ballWidth) {
                lives = lives - 1;
                if (lives == 0) {
                    lives = 3;
                    score = 0;
                    //soundPool.play(sample4, 1, 1, 0, 0, 1);
                }
                ballPosition.y = 1 + ballWidth;//back to top of screen

                //what horizontal direction should we use
                //for the next falling ball
                Random randomNumber = new Random();
                int startX = randomNumber.nextInt(screenWidth - ballWidth) + 1;
                ballPosition.x = startX + ballWidth;

                int ballDirection = randomNumber.nextInt(3);
                switch (ballDirection) {
                    case 0:
                        ballIsMovingLeft = true;
                        ballIsMovingRight = false;
                        break;

                    case 1:
                        ballIsMovingRight = true;
                        ballIsMovingLeft = false;
                        break;

                    case 2:
                        ballIsMovingLeft = false;
                        ballIsMovingRight = false;
                        break;
                }
            }

            //we hit the top of the screen
            if (ballPosition.y <= 0) {
                ballIsMovingDown = true;
                ballIsMovingUp = false;
                ballPosition.y = 1;
                //soundPool.play(sample2, 1, 1, 0, 0, 1);
            }

            //depending upon the two directions we should be
            //moving in adjust our x any positions
            if (ballIsMovingDown) {
                ballPosition.y += 6;
            }

            if (ballIsMovingUp) {
                ballPosition.y -= 10;
            }

            if (ballIsMovingLeft) {
                ballPosition.x -= 12;
            }

            if (ballIsMovingRight) {
                ballPosition.x += 12;
            }

            //Has ball hit racket
            if (ballPosition.y + ballWidth >= (racketPosition.y - racketHeight / 2)) {
                int halfRacket = racketWidth / 2;
                if (ballPosition.x + ballWidth > (racketPosition.x - halfRacket)
                        && ballPosition.x - ballWidth < (racketPosition.x + halfRacket)) {
                    //rebound the ball and play a sound
                    //soundPool.play(sample3, 1, 1, 0, 0, 1);
                    score++;
                    ballIsMovingUp = true;
                    ballIsMovingDown = false;
                    //now decide how to rebound the ball
                    if (ballPosition.x > racketPosition.x) {
                        ballIsMovingRight = true;
                        ballIsMovingLeft = false;

                    } else {
                        ballIsMovingRight = false;
                        ballIsMovingLeft = true;
                    }

                }
            }
            */
        }

        public void drawCourt() {

            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();
                //Paint paint = new Paint();
                canvas.drawColor(Color.BLACK);//the background
                paint.setColor(Color.argb(255, 0, 255, 255));
                paint.setTextSize(45);
                canvas.drawText("Money:" + playerMoney + " Winnings: " + winnings + " fps:" + fps, 20, 40, paint);

                /*
                $("#jackpot").text("Jackpot: " + jackpot);
                $("#playerMoney").text("Player Money: " + playerMoney);
                $("#playerTurn").text("Turn: " + turn);
                $("#playerWins").text("Wins: " + winNumber);
                $("#playerLosses").text("Losses: " + lossNumber);
                $("#playerWinRatio").text("Win Ratio: " + (winRatio * 100).toFixed(2) + "%");
                */
                canvas.drawText("Jackpot: " + jackpot + " playerTurn: " + turn, 20, 105, paint);
                canvas.drawText("playerLosses: " + lossNumber + " playerWins: " + winNumber, 20, 185, paint);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(75);
                canvas.drawText("playerWinRatio " + (winRatio * 100) + "%", 20, 265, paint);

                //canvas.drawText("You Won the $" + jackpot + " Jackpot!!", 20, 500, paint); //test code

                /*
                //Draw the squash racket
                canvas.drawRect(racketPosition.x - (racketWidth / 2),
                        racketPosition.y - (racketHeight / 2), racketPosition.x + (racketWidth / 2),
                        racketPosition.y + racketHeight, paint);

                //Draw the ball
                canvas.drawRect(ballPosition.x, ballPosition.y,
                        ballPosition.x + ballWidth, ballPosition.y + ballWidth, paint);
                */

                ourHolder.unlockCanvasAndPost(canvas);
            }

        }

        public void controlFPS() {
            long timeThisFrame = (System.currentTimeMillis() - lastFrameTime);
            long timeToSleep = 15 - timeThisFrame;
            if (timeThisFrame > 0) {
                fps = (int) (1000 / timeThisFrame);
            }
            if (timeToSleep > 0) {

                try {
                    ourThread.sleep(timeToSleep);
                } catch (InterruptedException e) {
                }

            }

            lastFrameTime = System.currentTimeMillis();
        }


        public void pause() {
            playingSlots = false;
            try {
                ourThread.join();
            } catch (InterruptedException e) {
            }

        }

        public void resume() {
            playingSlots = true;
            ourThread = new Thread(this);
            ourThread.start();
        }


        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {


            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:

                    if (motionEvent.getX() >= screenWidth / 2) {
                        //racketIsMovingRight = true;
                        //racketIsMovingLeft = false;
                    } else {
                        //racketIsMovingLeft = true;
                        //racketIsMovingRight = false;
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    //racketIsMovingRight = false;
                    //racketIsMovingLeft = false;
                    break;
            }
            return true;
        }



    }

    @Override
    protected void onStop() {
        super.onStop();

        while (true) {
            SlotMachineView.pause();
            break;
        }
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        SlotMachineView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SlotMachineView.pause();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SlotMachineView.pause();
            finish();
            return true;
        }
        return false;
    }

}
