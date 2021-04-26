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
    public Vector2 min = new Vector2(0, 0);
    public Vector2 max = new Vector2();

    public BattleCam() {
        super();
    }

    @Override
    public void update() {
        ArrayList<Vector3> pos = new ArrayList<>();
        Vector3 cameraPosition = position;

        for (Character chara : Main.players) {
            if (chara != null) {
                Vector2 vec = chara.b2body.getPosition();
                sizes.add(new Vector2(chara.width * 2, chara.height * 2));
                pos.add(new Vector3(vec.x, vec.y, 0));
            }
        }

        Vector3 targetPos = average(pos);
        cameraPosition.scl(ispeed);
        targetPos.scl(speed);
        cameraPosition.add(targetPos);

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

        if (width > this.max.x) {
            width = Math.min(width, this.max.x);
            height = width * hRatio;
        } else if (height > this.max.y) {
            height = Math.min(height, this.max.y);
            width = height * vRatio;
        }

        viewportWidth = width;
        viewportHeight = height;

        cameraPosition.x = Math.max(cameraPosition.x, width / 2);
        cameraPosition.y = Math.max(cameraPosition.y, height / 2);
        cameraPosition.x = Math.min(cameraPosition.x, this.max.x - width / 2);
        cameraPosition.y = Math.min(cameraPosition.y, this.max.y - height / 2);

        position.set(cameraPosition);

        super.update();
    }

    private Vector3 average(ArrayList<Vector3> list) {
        Vector2 min = new Vector2(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        for (Vector3 item : list) {
            if (min.x > item.x) {
                min.x = item.x;
            }

            if (min.y > item.y) {
                min.y = item.y;
            }
        }

        Vector2 max = new Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
        for (Vector3 item : list) {
            if (max.x < item.x) {
                max.x = item.x;
            }

            if (max.y < item.y) {
                max.y = item.y;
            }
        }

        Vector3 result = new Vector3(min.x + max.x, min.y + max.y, 0);

        result.x /= 2;
        result.y /= 2;

        return result;
    }

    private Vector2 max(ArrayList<Vector3> list) {
        float x = Float.NEGATIVE_INFINITY;
        float y = Float.NEGATIVE_INFINITY;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).x + sizes.get(i).x > x) x = list.get(i).x + sizes.get(i).x;
            if (list.get(i).y + sizes.get(i).y > y) y = list.get(i).y + sizes.get(i).y;
        }

        if (x > max.x) {
            x = max.x;
        }

        if (y > max.y) {
            y = max.y;
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

        if (x < min.x) {
            x = min.x;
        }

        if (y < min.y) {
            y = min.y;
        }

        return new Vector2(x, y);
    }
}
