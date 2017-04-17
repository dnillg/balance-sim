package com.dgiczi.balancesim.simulation;

import com.dgiczi.balancesim.simulation.model.SimulatorParams;
import com.dgiczi.balancesim.simulation.model.SimulatorState;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.JointType;
import org.jbox2d.dynamics.joints.WheelJointDef;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SceneSimulator {

    private static final float GROUND_WIDTH = 100000f;
    private static final float GROUND_HEIGHT = 0.1f;
    private static final float DEFAULT_DENSITY = 10f;
    private static final float GROUND_FRICTION = 1;
    private static final float GROUND_DENNSITY = 0;
    private static final int JOINT_FRQ = 100;
    private static final float JOINT_DAMPING_RATIO = 0f;
    private static final int GRAVITY = -100;
    private static final int VELOCITY_ITERATIONS = 80;
    private static final int POSITION_ITERATIONS = 30;
    private static final float INIT_PUSH_FORCE = 1000f;
    private static final float WHEEL_FRICTION = 1.25f;

    private final SimulatorParams params;
    private final World world;
    private final Joint wheelJoint;
    private final Body body;
    private final Body wheel;

    public SceneSimulator(SimulatorParams params) {
        this.params = params;
        this.world = createWorld(params);
        this.wheelJoint = world.getJointList();
        this.body = wheelJoint.getBodyA();
        this.wheel = wheelJoint.getBodyB();
    }

    public void init() {
        wheelJoint.getBodyA().applyForce(new Vec2(INIT_PUSH_FORCE, 0f), new Vec2(0f, 0f));
    }

    private World createWorld(SimulatorParams params) {
        Vec2 gravity = new Vec2(0, GRAVITY);
        World world = new World(gravity);

        addGround(world);
        addBalanceRobotParts(world, params);

        return world;
    }

    public static Joint addBalanceRobotParts(World world, SimulatorParams params) {
        Body wheel = addWheel(world, params);
        Body body = addBody(world, params);
        Joint joint = addJoint(world, params, wheel, body);
        return joint;
    }

    private static Joint addJoint(World world, SimulatorParams params, Body wheel, Body body) {
        WheelJointDef jointDef = new WheelJointDef();
        jointDef.initialize(body, wheel, new Vec2(0, (float) params.getWheelRadius()), new Vec2(0, 0));
        jointDef.localAxisA.set(1, 0);
        jointDef.collideConnected = false;
        jointDef.dampingRatio = JOINT_DAMPING_RATIO;
        jointDef.frequencyHz = JOINT_FRQ;
        jointDef.type = JointType.WHEEL;
        jointDef.maxMotorTorque = (float) params.getMaxTorque();
        jointDef.enableMotor = true;
        return world.createJoint(jointDef);
    }

    public static Body addBody(World world, SimulatorParams params) {
        PolygonShape bodyShape = new PolygonShape();
        bodyShape.setAsBox((float) params.getBodyWidth(), (float) params.getBodyHeight());
        bodyShape.m_centroid.set((float) params.getCentroidX(), (float) params.getCentroidY());

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        float posY = (float) (params.getWheelRadius() + params.getBodyHeight());
        bodyDef.position = new Vec2(0f, posY);
        Body body = world.createBody(bodyDef);

        body.createFixture(bodyShape, DEFAULT_DENSITY);

        MassData massData = new MassData();
        body.getMassData(massData);
        massData.mass = (float)params.getBodyMass();
        massData.center.set((float)params.getCentroidX(), (float) params.getCentroidY());
        body.setMassData(massData);
        return body;
    }

    public static Body addWheel(World world, SimulatorParams params) {
        CircleShape wheelShape = new CircleShape();
        wheelShape.setRadius((float) params.getWheelRadius());

        BodyDef wheelDef = new BodyDef();
        wheelDef.type = BodyType.DYNAMIC;
        wheelDef.position = new Vec2(0, (float) params.getWheelRadius());
        Body wheel = world.createBody(wheelDef);

        FixtureDef wheelFixture = new FixtureDef();
        wheelFixture.shape = wheelShape;
        wheelFixture.friction = WHEEL_FRICTION;
        wheelFixture.density = DEFAULT_DENSITY;
        wheel.createFixture(wheelFixture);

        MassData massData = new MassData();
        wheel.getMassData(massData);
        massData.mass = (float)params.getWheelsMass();
        wheel.setMassData(massData);

        return wheel;
    }

    public static Body addGround(World world) {
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(GROUND_WIDTH, GROUND_HEIGHT);

        BodyDef groundDef = new BodyDef();
        groundDef.type = BodyType.STATIC;
        groundDef.position.set(0, 0);
        Body ground = world.createBody(groundDef);

        FixtureDef groundFixture = new FixtureDef();
        groundFixture.shape = groundShape;
        groundFixture.density = GROUND_DENNSITY;
        groundFixture.friction = GROUND_FRICTION;
        ground.createFixture(groundFixture);

        return ground;
    }

    public void step(double dt) {
        world.step((float) dt, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    }

    public SimulatorState getState() {
        double distance = wheel.getPosition().x;
        double angle = Math.toDegrees(body.getAngle());
        double wheelSpeed = wheel.getLinearVelocity().x;
        double angularSpeed = Math.toDegrees(body.getAngularVelocity());

        SimulatorState state = new SimulatorState(distance, angle, wheelSpeed, angularSpeed);
        return state;
    }
}
