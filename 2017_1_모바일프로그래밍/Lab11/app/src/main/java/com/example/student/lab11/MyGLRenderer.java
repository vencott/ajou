package com.example.student.lab11;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer
{
	private static final String TAG = "MyGLRenderer";
	private float[] mModelMatrix = new float[16];
	private float[] mViewMatrix = new float[16];
	private float[] mProjectionMatrix = new float[16];
	private float[] mLightModelMatrix = new float[16];
	private final FloatBuffer mCubePositions;
	private final FloatBuffer mCubeColors;
	private final FloatBuffer mCubeNormals;
	private final float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
	private final float[] mLightPosInWorldSpace = new float[4];
	private int mPerVertexProgramHandle;
	private final int mBytesPerFloat = 4;
	private final int mPositionDataSize = 3;
	private final int mColorDataSize = 4;
	private final int mNormalDataSize = 3;
	private float theta = 0;
	public boolean ambientOn = false;
	public boolean diffuseOn = false;


	public MyGLRenderer()
	{
		mCubePositions = ByteBuffer.allocateDirect(cubePositionData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mCubePositions.put(cubePositionData).position(0);

		mCubeColors = ByteBuffer.allocateDirect(cubeColorData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mCubeColors.put(cubeColorData).position(0);

		mCubeNormals = ByteBuffer.allocateDirect(cubeNormalData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mCubeNormals.put(cubeNormalData).position(0);
	}

	protected String getVertexShader()
	{
		final String vertexShader =
				"uniform mat4 u_M;      \n"
						+ "uniform mat4 u_V;      \n"
						+ "uniform mat4 u_P;      \n"
//여기서부터 다섯개 추가
						+ "uniform vec3 u_L;      \n"
						+ "uniform vec3 u_LightColor;      \n"
						+ "uniform vec3 u_AmbientLighting;      \n"

						+ "uniform int u_AmbientOn;      \n"
						+ "uniform int u_DiffuseOn;      \n"

						+ "attribute vec4 a_Position;     \n"
						+ "attribute vec4 a_Color;        \n"
						+ "attribute vec3 a_Normal;       \n"

						+ "varying vec4 v_Color;          \n"

						+ "void main()                    \n"
						+ "{                              \n"
						+ "   vec3 wsNormal = vec3(u_M*vec4(a_Normal, 0.0));                     \n"
						+ "   float diffuse = max(-dot(wsNormal, u_L), 0.0);                      \n"

						+ "   if(u_AmbientOn == 1 && u_DiffuseOn == 1) {                     \n"
						+ "   v_Color.xyz = (diffuse * u_LightColor) + a_Color.xyz* 0.0 + u_AmbientLighting;                       \n"
						+ "   v_Color.w = a_Color.w;											       \n"
						+ "   }                       \n"

						+ "   else if(u_AmbientOn == 1 && u_DiffuseOn == 0) {                       \n"
						+ "   v_Color.xyz = a_Color.xyz + u_AmbientLighting;                     \n"
						+ "   v_Color.w = a_Color.w;											       \n"
						+ "   }                       \n"

						+ "   else if(u_AmbientOn == 0 && u_DiffuseOn == 1) {                       \n"
						+ "   v_Color.xyz = (diffuse * u_LightColor) + a_Color.xyz* 0.0;                       \n"
						+ "   }                       \n"

						+ "   else if(u_AmbientOn == 0 && u_DiffuseOn == 0) {                       \n"
						+ "   v_Color = a_Color * 0.0;                       \n"
						+ "   }                       \n"

						+ "   gl_Position = u_P * u_V * u_M * a_Position;                        \n"
						+ "}                                                                     \n";

		return vertexShader;
	}

	protected String getFragmentShader()
	{
		final String fragmentShader =
				"precision mediump float;       \n"		// Set the default precision to medium. We don't need as high of a
						// precision in the fragment shader.
						+ "varying vec4 v_Color;          \n"		// This is the color from the vertex shader interpolated across the
						// triangle per fragment.
						+ "void main()                    \n"		// The entry point for our fragment shader.
						+ "{                              \n"
						+ "   gl_FragColor = v_Color;     \n"		// Pass the color directly through the pipeline.
						+ "}                              \n";

		return fragmentShader;
	}

	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config)
	{
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);


		final String vertexShader = getVertexShader();
		final String fragmentShader = getFragmentShader();

		final int vertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
		final int fragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
		mPerVertexProgramHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
				new String[] {"a_Position",  "a_Color", "a_Normal"});
	}

	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height)
	{
		GLES20.glViewport(0, 0, width, height);

		final float ratio = (float) width / height;
		final float left = -ratio;
		final float right = ratio;
		final float bottom = -1.0f;
		final float top = 1.0f;
		final float near = 1.0f;
		final float far = 10.0f;

		Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
	}

	@Override
	public void onDrawFrame(GL10 glUnused)
	{
		theta += 1;
		if(theta >= 360)
			theta = 0;

		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		GLES20.glUseProgram(mPerVertexProgramHandle);
		Matrix.setIdentityM(mModelMatrix, 0);
		int  uM_h = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_M");
		GLES20.glUniformMatrix4fv(uM_h, 1, false, mModelMatrix, 0);

		float eyeX = (float)5 * (float)Math.cos((theta/180) * Math.PI);
		float eyeY = (float)5 * (float)Math.sin((theta/180) * Math.PI);
		float eyeZ = 3.0f;

		Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, 0, 0, 0, 0, 0, 1);
		int  uV_h = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_V");
		GLES20.glUniformMatrix4fv(uV_h, 1, false, mViewMatrix, 0);

		int  uP_h = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_P");
		GLES20.glUniformMatrix4fv(uP_h, 1, false, mProjectionMatrix, 0);

		Matrix.setIdentityM(mLightModelMatrix, 0);
		Matrix.translateM(mLightModelMatrix, 0, 5.0f, 5.0f, 1.0f);
		Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);

		int	uLightPos_h = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_LightPos");
		GLES20.glUniform3f(uLightPos_h, mLightPosInWorldSpace[0], mLightPosInWorldSpace[1], mLightPosInWorldSpace[2]);

		// 여기서부터 내가짠 코드
		int uL_h = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_L");
		GLES20.glUniform3f(uL_h, -1.0f, -0.8f, -0.3f);

		int uLightColor_h = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_LightColor");
		GLES20.glUniform3f(uLightColor_h, 1.0f, 0.2f, 0.2f);

		int uAmbientLighting_h = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_AmbientLighting");
		GLES20.glUniform3f(uAmbientLighting_h, 0.2f, 0.2f, 0.2f);

		if(ambientOn) {
			int uAmbiOn_h = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_AmbientOn");
			GLES20.glUniform1i(uAmbiOn_h, 1);
		}
		else {
			int uAmbiOn_h = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_AmbientOn");
			GLES20.glUniform1i(uAmbiOn_h, 0);
		}

		if(diffuseOn) {
			int uDiffOn_h = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_DiffuseOn");
			GLES20.glUniform1i(uDiffOn_h, 1);
		}
		else {
			int uDiffOn_h = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_DiffuseOn");
			GLES20.glUniform1i(uDiffOn_h, 0);
		}


		drawCube();
	}

	private void drawCube()
	{
		int pos_h;
		int col_h;
		int norm_h;

		pos_h = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Position");
		col_h = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Color");
		norm_h = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Normal");


		mCubePositions.position(0);
		GLES20.glVertexAttribPointer(pos_h, mPositionDataSize, GLES20.GL_FLOAT, false,
				0, mCubePositions);

		GLES20.glEnableVertexAttribArray(pos_h);

		mCubeColors.position(0);
		GLES20.glVertexAttribPointer(col_h, mColorDataSize, GLES20.GL_FLOAT, false,
				0, mCubeColors);

		GLES20.glEnableVertexAttribArray(col_h);

		mCubeNormals.position(0);
		GLES20.glVertexAttribPointer(norm_h, mNormalDataSize, GLES20.GL_FLOAT, false,
				0, mCubeNormals);

		GLES20.glEnableVertexAttribArray(norm_h);



		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
	}

	private int compileShader(final int shaderType, final String shaderSource)
	{
		int shaderHandle = GLES20.glCreateShader(shaderType);

		if (shaderHandle != 0)
		{
			GLES20.glShaderSource(shaderHandle, shaderSource);
			GLES20.glCompileShader(shaderHandle);

			final int[] compileStatus = new int[1];
			GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

			if (compileStatus[0] == 0)
			{
				Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
				GLES20.glDeleteShader(shaderHandle);
				shaderHandle = 0;
			}
		}

		if (shaderHandle == 0)
		{
			throw new RuntimeException("Error creating shader.");
		}

		return shaderHandle;
	}

	private int createAndLinkProgram(final int vertexShaderHandle, final int fragmentShaderHandle, final String[] attributes)
	{
		int programHandle = GLES20.glCreateProgram();

		if (programHandle != 0)
		{
			GLES20.glAttachShader(programHandle, vertexShaderHandle);
			GLES20.glAttachShader(programHandle, fragmentShaderHandle);

			if (attributes != null)
			{
				final int size = attributes.length;
				for (int i = 0; i < size; i++)
				{
					GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
				}
			}

			GLES20.glLinkProgram(programHandle);

			final int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

			if (linkStatus[0] == 0)
			{
				Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
				GLES20.glDeleteProgram(programHandle);
				programHandle = 0;
			}
		}

		if (programHandle == 0)
		{
			throw new RuntimeException("Error creating program.");
		}

		return programHandle;
	}


	private float[] cubeColorData =
			{
					// Front face
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,

					// Right face
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,

					// Back face
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,

					// Left face
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,

					// Top face
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,

					// Bottom face
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f, 1.0f,
			};

	private float[] cubePositionData =
			{
					// Front face
					-1.0f, 1.0f, 1.0f,
					-1.0f, -1.0f, 1.0f,
					1.0f, 1.0f, 1.0f,
					-1.0f, -1.0f, 1.0f,
					1.0f, -1.0f, 1.0f,
					1.0f, 1.0f, 1.0f,

					// Right face
					1.0f, 1.0f, 1.0f,
					1.0f, -1.0f, 1.0f,
					1.0f, 1.0f, -1.0f,
					1.0f, -1.0f, 1.0f,
					1.0f, -1.0f, -1.0f,
					1.0f, 1.0f, -1.0f,

					// Back face
					1.0f, 1.0f, -1.0f,
					1.0f, -1.0f, -1.0f,
					-1.0f, 1.0f, -1.0f,
					1.0f, -1.0f, -1.0f,
					-1.0f, -1.0f, -1.0f,
					-1.0f, 1.0f, -1.0f,

					// Left face
					-1.0f, 1.0f, -1.0f,
					-1.0f, -1.0f, -1.0f,
					-1.0f, 1.0f, 1.0f,
					-1.0f, -1.0f, -1.0f,
					-1.0f, -1.0f, 1.0f,
					-1.0f, 1.0f, 1.0f,

					// Top face
					-1.0f, 1.0f, -1.0f,
					-1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, -1.0f,
					-1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, 1.0f,
					1.0f, 1.0f, -1.0f,

					// Bottom face
					1.0f, -1.0f, -1.0f,
					1.0f, -1.0f, 1.0f,
					-1.0f, -1.0f, -1.0f,
					1.0f, -1.0f, 1.0f,
					-1.0f, -1.0f, 1.0f,
					-1.0f, -1.0f, -1.0f,
			};



	private float[] cubeNormalData =
			{
					// Front face
					0.0f, 0.0f, 1.0f,
					0.0f, 0.0f, 1.0f,
					0.0f, 0.0f, 1.0f,
					0.0f, 0.0f, 1.0f,
					0.0f, 0.0f, 1.0f,
					0.0f, 0.0f, 1.0f,

					// Right face
					1.0f, 0.0f, 0.0f,
					1.0f, 0.0f, 0.0f,
					1.0f, 0.0f, 0.0f,
					1.0f, 0.0f, 0.0f,
					1.0f, 0.0f, 0.0f,
					1.0f, 0.0f, 0.0f,

					// Back face
					0.0f, 0.0f, -1.0f,
					0.0f, 0.0f, -1.0f,
					0.0f, 0.0f, -1.0f,
					0.0f, 0.0f, -1.0f,
					0.0f, 0.0f, -1.0f,
					0.0f, 0.0f, -1.0f,

					// Left face
					-1.0f, 0.0f, 0.0f,
					-1.0f, 0.0f, 0.0f,
					-1.0f, 0.0f, 0.0f,
					-1.0f, 0.0f, 0.0f,
					-1.0f, 0.0f, 0.0f,
					-1.0f, 0.0f, 0.0f,

					// Top face
					0.0f, 1.0f, 0.0f,
					0.0f, 1.0f, 0.0f,
					0.0f, 1.0f, 0.0f,
					0.0f, 1.0f, 0.0f,
					0.0f, 1.0f, 0.0f,
					0.0f, 1.0f, 0.0f,

					// Bottom face
					0.0f, -1.0f, 0.0f,
					0.0f, -1.0f, 0.0f,
					0.0f, -1.0f, 0.0f,
					0.0f, -1.0f, 0.0f,
					0.0f, -1.0f, 0.0f,
					0.0f, -1.0f, 0.0f
			};

	private float[] redCubeColorData =
			{
					// Front face
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,

					// Right face
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,

					// Back face
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,

					// Left face
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,

					// Top face
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,

					// Bottom face
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
					1.0f, 0.0f, 0.0f, 1.0f,
			};

	private float[] greenCubeColorData =
			{
					// Front face
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,

					// Right face
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,

					// Back face
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,

					// Left face
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,

					// Top face
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,

					// Bottom face
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
					0.0f, 1.0f, 0.0f, 1.0f,
			};

	private float[] blueCubeColorData =
			{
					// Front face
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,

					// Right face
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,

					// Back face
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,

					// Left face
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,

					// Top face
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,

					// Bottom face
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
					0.0f, 0.0f, 1.0f, 1.0f,
			};
}
