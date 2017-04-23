package com.dgiczi.balancesim.simulation;

import com.dgiczi.balancesim.simulation.actions.UserAction;
import com.dgiczi.balancesim.simulation.model.BalanceBot;
import com.dgiczi.balancesim.simulation.model.SimulationParams;
import com.dgiczi.balancesim.simulation.model.SimulationState;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.JointType;
import org.jbox2d.dynamics.joints.WheelJoint;
import org.jbox2d.dynamics.joints.WheelJointDef;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SceneSimulator {

    public static final double SCALE = 0.1;
    private static final float GROUND_WIDTH = 100000f;
    private static final float GROUND_HEIGHT = 0.1f;
    private static final float DEFAULT_DENSITY = 1f;
    private static final float GROUND_FRICTION = 1;
    private static final float GROUND_DENSITY = 0;
    private static final int JOINT_FRQ = 100;
    private static final float JOINT_DAMPING_RATIO = 0f;
    private static final float GRAVITY = -100f;
    private static final int VELOCITY_ITERATIONS = 80;
    private static final int POSITION_ITERATIONS = 30;
    private static final float INIT_PUSH_FORCE = -100000f;
    private static final float WHEEL_FRICTION = 1.50f;
    private static final float BODY_FRICTION = 1.50f;

    private final SimulationParams params;
    private final World world;
    private final BalanceBot balanceBot;


    public SceneSimulator(SimulationParams params) {
        this.params = params.withScale(SCALE);
        this.world = createWorld(this.params);

        BalanceBot balanceBot = createBalanceBot();
        this.balanceBot = balanceBot;
    }

    private BalanceBot createBalanceBot() {
        WheelJoint wheelJoint = (WheelJoint) world.getJointList();
        Body body = wheelJoint.getBodyA();
        Body wheel = wheelJoint.getBodyB();
        return new BalanceBot(body, wheel, wheelJoint);
    }

    public void init() {
        balanceBot.getBody().applyForce(new Vec2(INIT_PUSH_FORCE, 0f), new Vec2(0f, 0f));
    }

    private World createWorld(SimulationParams params) {
        Vec2 gravity = new Vec2(0, GRAVITY);
        World world = new World(gravity);

        addGround(world);
        addBalanceRobotParts(world, params);

        return world;
    }

    public static Joint addBalanceRobotParts(World world, SimulationParams params) {
        Body wheel = addWheel(world, params);
        Body body = addBody(world, params);
        Joint joint = addJoint(world, params, wheel, body);
        return joint;
    }

    private static Joint addJoint(World world, SimulationParams params, Body wheel, Body body) {
        WheelJointDef jointDef = new WheelJointDef();
        jointDef.initialize(body, wheel, new Vec2(0, (float) params.getWheelDiameter()), new Vec2(0, 0));
        jointDef.localAxisA.set(1, 0);
        jointDef.collideConnected = false;
        jointDef.dampingRatio = JOINT_DAMPING_RATIO;
        jointDef.frequencyHz = JOINT_FRQ;
        jointDef.type = JointType.WHEEL;
        jointDef.maxMotorTorque = (float) params.getMaxTorque();
        WheelJoint joint = (WheelJoint)world.createJoint(jointDef);
        return joint;
    }

    public static Body addBody(World world, SimulationParams params) {
        PolygonShape bodyShape = new PolygonShape();
        bodyShape.setAsBox((float) params.getBodyWidth(), (float) params.getBodyHeight());
        bodyShape.m_centroid.set((float) params.getCentroidX(), (float) params.getCentroidY());

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        float posY = (float) (params.getWheelDiameter() + params.getBodyHeight());
        bodyDef.position = new Vec2(0f, posY);
        Body body = world.createBody(bodyDef);

        FixtureDef bodyFixture = new FixtureDef();
        bodyFixture.shape = bodyShape;
        bodyFixture.friction = BODY_FRICTION;
        bodyFixture.density = DEFAULT_DENSITY;
        body.createFixture(bodyFixture);

        MassData massData = new MassData();
        body.getMassData(massData);
        //massData.mass = (float) params.getBodyMass();
        massData.center.set((float) params.getCentroidX(), (float) params.getCentroidY());
        body.setMassData(massData);
        return body;
    }

    public static Body addWheel(World world, SimulationParams params) {
        CircleShape wheelShape = new CircleShape();
        wheelShape.setRadius((float) params.getWheelDiameter());

        BodyDef wheelDef = new BodyDef();
        wheelDef.type = BodyType.DYNAMIC;
        wheelDef.position = new Vec2(0, (float) params.getWheelDiameter());
        Body wheel = world.createBody(wheelDef);

        FixtureDef wheelFixture = new FixtureDef();
        wheelFixture.shape = wheelShape;
        wheelFixture.friction = WHEEL_FRICTION;
        wheelFixture.density = DEFAULT_DENSITY;
        wheel.createFixture(wheelFixture);

        MassData massData = new MassData();
        wheel.getMassData(massData);
        //massData.mass = (float) params.getWheelsMass();
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
        groundFixture.density = GROUND_DENSITY;
        groundFixture.friction = GROUND_FRICTION;
        ground.createFixture(groundFixture);

        return ground;
    }

    public synchronized void step(double dt) {
        world.step((float) dt, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    }

    public SimulationState getState() {
        double posX = balanceBot.getWheel().getPosition().x;
        double posY =  balanceBot.getWheel().getPosition().y - params.getWheelDiameter();
        double angle = Math.toDegrees( balanceBot.getBody().getAngle());
        double speed = balanceBot.getWheel().getLinearVelocity().x;
        double angularSpeed = Math.toDegrees(balanceBot.getBody().getAngularVelocity());

        SimulationState state = new SimulationState(posX, posY, angle, speed, angularSpeed).withScale(1 / SCALE);
        return state;
    }

    public synchronized void applyUserAction(UserAction action) {
        action.apply(balanceBot, world);
    }
}
