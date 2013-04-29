package river.raid.game;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import android.util.Log;




/*
	Scena gry po utraceniu dostępych żyć
*/
public class GameOverScene extends MenuScene implements IOnSceneTouchListener {

	boolean done;
	boolean shown = false;
	BaseActivity activity;
	private Sprite window;
	private float height;
	private int collectedPoints;
	public GameOverScene(Camera mCamera, int points) {
		super(mCamera);
		activity = BaseActivity.getSharedInstance();
		setBackgroundEnabled(false);
		collectedPoints = points;
		
		
		window = new Sprite(0, 0, activity.regGamePopup, activity.getVertexBufferObjectManager());
		height = mCamera.getHeight();
		final int x = (int) (mCamera.getWidth() / 2 - window.getWidth() / 2);
		final int y = (int) (mCamera.getHeight() / 2 - window.getHeight() / 2);
		Sprite menubutton = new Sprite(0, 0, activity.regMenuButton, activity.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				activity.mCurrentScene.back();
				
				return true;
			}
		};

		Sprite submitbutton = new Sprite(0, 0, activity.regSubmitButton, activity.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				closePopup(window,height);
				return true;
			}
		};
		
		Sprite againbutton = new Sprite(0, 0, activity.regPlayAgainButton, activity.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				((GameScene) activity.mCurrentScene).resetValues();

				return true;
			}
		};
		Text result = new Text(0, 0, activity.jellyFont, activity.getString(R.string.gameOverPoints)+" "+collectedPoints, BaseActivity.getSharedInstance().getVertexBufferObjectManager());


		done = false;
		menubutton.setPosition(window.getWidth() / 2 - menubutton.getWidth() / 2, window.getHeight() - menubutton.getHeight()*1.5f);
		againbutton.setPosition(againbutton.getWidth() / 2, window.getHeight() - againbutton.getHeight()*1.5f);
		submitbutton.setPosition(window.getWidth() - submitbutton.getWidth()*1.5f,  window.getHeight() - submitbutton.getHeight()*1.5f);
		result.setPosition(window.getWidth() / 2 - result.getWidth() / 2,  result.getHeight()/2);
		window.attachChild(result);
		window.attachChild(menubutton);
		window.attachChild(submitbutton);
		window.attachChild(againbutton);
		window.setPosition(x, mCamera.getHeight() + window.getHeight());
		MoveYModifier mod = new MoveYModifier(0.5f, window.getY(), y) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				done = true;
				shown = true;
			}
		};
		attachChild(window);
		window.registerEntityModifier(mod);
		registerTouchArea(menubutton);
		registerTouchArea(submitbutton);
		registerTouchArea(againbutton);
		setTouchAreaBindingOnActionDownEnabled(true);

	}

	@Override
	public boolean onSceneTouchEvent(Scene arg0, TouchEvent arg1) {
		if (!done)
			return true;
		// ((GameScene) activity.mCurrentScene).resetValues();
		return false;
	}

	/*
		Animacja chowania popup po czym jego zamkniecie
	*/
	private void closePopup(Sprite popup, float y){
		MoveYModifier mod = new MoveYModifier(0.5f, popup.getY(),y+popup.getY()) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				shown = false;
				closeMenuScene();
			}
		};
		popup.registerEntityModifier(mod);
	}
}
