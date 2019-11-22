package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;

public class Maths {
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector2f translation, float rotation,  Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate(rotation, new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}

	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate(rx, new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate(ry, new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate(rz, new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
		return matrix;
	}
	
	//use current and original direction vectors to determine transformation
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f originalUpDirection, Vector3f newUpDirection, Vector3f originalForwardDirection, Vector3f newForwardDirection, float scale) {
		Vector3f upCross = Vector3f.cross(originalUpDirection, newUpDirection, null);
		if (isVectorZero(upCross)) {
			upCross = Vector3f.cross(originalUpDirection, originalForwardDirection, null);
		}
		upCross.normalise();
		float upAngle = Vector3f.angle(originalUpDirection, newUpDirection);
		Vector3f intermediateForwards = rotateVector(originalForwardDirection,upCross,upAngle);
		Vector3f forwardCross = Vector3f.cross(intermediateForwards, newForwardDirection, null);
		if (isVectorZero(forwardCross)) {
			forwardCross = newUpDirection;
		}
		forwardCross.normalise();
		float forwardAngle = Vector3f.angle(intermediateForwards, newForwardDirection);
		
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate(forwardAngle, forwardCross, matrix, matrix);
		Matrix4f.rotate(upAngle, upCross, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Vector3f reverseFacing = negateVector(camera.getFacingDirection());
		Vector3f cameraCross = Vector3f.cross(reverseFacing, new Vector3f(0,0,1), null);
		if (isVectorZero(cameraCross)) {
			cameraCross = new Vector3f(0,1,0);
		}
		cameraCross.normalise();
		float cameraAngle = Vector3f.angle(reverseFacing, new Vector3f(0,0,1));
		Vector3f intermediateUpDirection = rotateVector(camera.getRelativeUpDirection(),cameraCross,cameraAngle);
		Vector2f intermUpDirXY = new Vector2f(intermediateUpDirection.x, intermediateUpDirection.y);
		//based on the new upwards direction, need to change the roll accordingly
		float rollAngle = Vector2f.angle(intermUpDirXY, new Vector2f(0,1));
		if(intermediateUpDirection.x>0) {
			rollAngle = -rollAngle;
		}
		Matrix4f.rotate(rollAngle, new Vector3f(0,0,-1), viewMatrix, viewMatrix);
		Matrix4f.rotate(cameraAngle, cameraCross, viewMatrix, viewMatrix);
		Vector3f cameraPosition = camera.getPosition();
		Matrix4f.translate(new Vector3f(-cameraPosition.x,-cameraPosition.y,-cameraPosition.z), viewMatrix, viewMatrix);
		return viewMatrix;
	}
	
	public static Vector3f rotateVector(Vector3f inVector, Vector3f inAxis, float angle) {
		Vector3f axis = copyVector(inAxis);
		axis.normalise();
		Matrix4f rotationMatrix = new Matrix4f();
		rotationMatrix.setIdentity();
		Matrix4f.rotate(angle, axis, rotationMatrix, rotationMatrix);
		Vector4f rotateVector = new Vector4f(inVector.x, inVector.y, inVector.z, 1.0f);
		Matrix4f.transform(rotationMatrix, rotateVector, rotateVector);
		return new Vector3f(rotateVector.x, rotateVector.y, rotateVector.z);
	}
	
	public static boolean isVectorZero(Vector3f vector) {
		return (vector.x == 0 && vector.y == 0  && vector.z == 0);
	}
	
	public static Vector3f copyVector(Vector3f vector) {
		return new Vector3f(vector.x, vector.y, vector.z);
	}
	
	public static Vector2f copyVector(Vector2f vector) {
		return new Vector2f(vector.x, vector.y);
	}
	
	public static Vector3f scaledVector(Vector3f vector, float scale) {
		return new Vector3f(vector.x*scale, vector.y*scale, vector.z*scale);
	}
	
	public static Vector2f scaledVector(Vector2f vector, float scale) {
		return new Vector2f(vector.x*scale, vector.y*scale);
	}
	
	public static Vector2f scaledComponentwiseVector(Vector2f vector, Vector2f scale) {
		return new Vector2f(vector.x*scale.x, vector.y*scale.y);
	}
	
	public static Vector3f normScaledVector(Vector3f vector, float scale) {
		Vector3f tempVector = copyVector(vector);
		tempVector.normalise();
		return new Vector3f(tempVector.x*scale, tempVector.y*scale,tempVector.z*scale);
	}
	
	public static Vector2f normScaledVector(Vector2f vector, float scale) {
		Vector2f tempVector = copyVector(vector);
		tempVector.normalise();
		return new Vector2f(tempVector.x*scale, tempVector.y*scale);
	}
	
	public static Vector3f negateVector(Vector3f vector) {
		return new Vector3f(-vector.x, -vector.y, -vector.z);
	}
	
	public static float clampFloat(float input, float min, float max) {
		float output = input;
		if(input>max) {
			output = max;
		}
		if (input<min) {
			output = min;
		}
		return output;
	}
	
	//check for a spherical collision between two points, given ther radii
	public static boolean isSphericalCollision(Vector3f pos1, Vector3f pos2, float rad1, float rad2) {
		Vector3f vectorBetween = Vector3f.sub(pos2, pos1, null);
		float distanceSquared = vectorBetween.lengthSquared();
		if(distanceSquared <= (rad1 + rad2)*(rad1 + rad2)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static Vector2f convertToCentred(Vector2f inCoords) {
		return new Vector2f(2*inCoords.x - 1, 2*(1 - inCoords.y) - 1);
	}
	
	public static Vector2f convertToTopLeft(Vector2f inCoords) {
		return new Vector2f((inCoords.x+1)/2,1-(inCoords.y+1)/2);
	}
	
	public static Vector3f generateRandomVector() {
		return new Vector3f((float) (Math.random()), (float) (Math.random()), (float) (Math.random()));
	}
	
	public static Vector4f getGLCoords(Vector3f worldPos, Matrix4f viewMatrix, Matrix4f projectionMatrix) {
		Vector4f totalWorldPos = new Vector4f(worldPos.x, worldPos.y, worldPos.z, 1);
		Matrix4f.transform(viewMatrix, totalWorldPos, totalWorldPos);
		Matrix4f.transform(projectionMatrix, totalWorldPos, totalWorldPos);
		return totalWorldPos;
	}	
	
}
