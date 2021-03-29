package ca.error404.bytefyte.objects;

import ca.error404.bytefyte.Main;
import ca.error404.bytefyte.chars.Character;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class BattleCam extends OrthographicCamera {
    final float speed = 0.1f, ispeed = 1.0f-speed, hRatio = 0.5625f, vRatio = 1.777777778f;
    ArrayList<Vector2> sizes = new ArrayList<>();

    public BattleCam() {
        super();
    }

    @Override
    public void update() {
        ArrayList<Vector3> pos = new ArrayList<>();
        Vector3 cameraPosition = position;

        for (Character chara : Main.players) {
            Vector2 vec = chara.b2body.getPosition();
            sizes.add(new Vector2(chara.width * 2, chara.height * 2));
            pos.add(new Vector3(vec.x, vec.y, 0));
        }

        Vector3 targetPos = average(pos);
        cameraPosition.scl(ispeed);
        targetPos.scl(speed);
        cameraPosition.add(targetPos);

        position.set(cameraPosition);

        float width = 2.5f, height = width * hRatio;

        Vector2 max = max(pos);
        Vector2 min = min(pos);

        width = Math.max(max.x - min.x, width);
        height = Math.max(max.y - min.y, height);

        if (height > width * hRatio) {
            width = height * vRatio;
        } else {
            height = width * hRatio;
        }

        viewportWidth = width;
        viewportHeight = height;

        super.update();
    }

    private Vector3 average(ArrayList<Vector3> list) {
        Vector3 result = new Vector3();

        for (Vector3 item : list) {
            result.x += item.x;
            result.y += item.y;
        }

        result.x /= list.size();
        result.y /= list.size();

        return result;
    }

    private Vector2 max(ArrayList<Vector3> list) {
        float x = Float.NEGATIVE_INFINITY;
        float y = Float.NEGATIVE_INFINITY;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).x + sizes.get(i).x > x) x = list.get(i).x + sizes.get(i).x;
            if (list.get(i).y + sizes.get(i).y > y) y = list.get(i).y + sizes.get(i).y;
        }

        return new Vector2(x, y);
    }

    private Vector2 min(ArrayList<Vector3> list) {
        float x = Float.POSITIVE_INFINITY;
        float y = Float.POSITIVE_INFINITY;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).x - sizes.get(i).x < x) x = list.get(i).x - sizes.get(i).x;
            if (list.get(i).y - sizes.get(i).y < y) y = list.get(i).y - sizes.get(i).y;
        }

        return new Vector2(x, y);
    }
}
