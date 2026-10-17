package com.yonn2222.essentialsxfixes.ext;

import com.yonn2222.essentialsxfixes.Messages;
import com.yonn2222.essentialsxfixes.route.Tokens;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.List;
import java.util.Locale;

final class Teleport {
    private Teleport() {
    }

    static int run(Call call) {
        call.require("essentials.tp");
        int size = call.tokens().size();
        if (size == 0) {
            throw new Failure("bad-arguments");
        }
        List<Entity> subjects;
        int next;
        if (Tokens.coordinate(call.token(0))) {
            if (call.executor() == null) {
                throw new Failure("no-target");
            }
            subjects = List.of(call.executor());
            next = 0;
        } else if (size == 1) {
            if (call.executor() == null) {
                throw new Failure("no-target");
            }
            subjects = List.of(call.executor());
            next = 0;
        } else {
            subjects = call.entities(0);
            next = 1;
        }
        if (!Tokens.coordinate(call.token(next))) {
            Entity destination = call.single(next);
            Location target = destination.getLocation();
            return move(call, subjects, target, destination.getName());
        }
        Location base = call.origin();
        Location target = position(call, base, next);
        int index = next + 3;
        if (call.has(index) && call.token(index).equalsIgnoreCase("facing")) {
            facing(call, target, index + 1);
        } else if (call.has(index) && call.has(index + 1)) {
            target.setYaw((float) angle(call.token(index), base.getYaw()));
            target.setPitch((float) angle(call.token(index + 1), base.getPitch()));
        } else if (call.has(index)) {
            throw new Failure("bad-arguments");
        } else {
            target.setYaw(Float.NaN);
        }
        String where = format(target.getX()) + " " + format(target.getY()) + " " + format(target.getZ());
        return move(call, subjects, target, where);
    }

    private static int move(Call call, List<Entity> subjects, Location target, String where) {
        int moved = 0;
        for (Entity entity : subjects) {
            Location destination = target.clone();
            if (Float.isNaN(destination.getYaw())) {
                destination.setYaw(entity.getLocation().getYaw());
                destination.setPitch(entity.getLocation().getPitch());
            }
            if (entity.teleport(destination)) {
                moved++;
            }
        }
        String who = subjects.size() == 1 ? subjects.get(0).getName() : String.valueOf(subjects.size());
        call.reply("teleported", Messages.text("who", who), Messages.text("where", where));
        return moved;
    }

    private static Location position(Call call, Location base, int index) {
        String x = call.token(index);
        String y = call.token(index + 1);
        String z = call.token(index + 2);
        boolean local = x.startsWith("^") || y.startsWith("^") || z.startsWith("^");
        if (local) {
            if (!(x.startsWith("^") && y.startsWith("^") && z.startsWith("^"))) {
                throw new Failure("bad-arguments");
            }
            return localPosition(base, offset(x), offset(y), offset(z));
        }
        Location out = base.clone();
        out.setX(axis(x, base.getX(), true));
        out.setY(axis(y, base.getY(), false));
        out.setZ(axis(z, base.getZ(), true));
        return out;
    }

    private static double axis(String token, double current, boolean center) {
        if (token.startsWith("~")) {
            return current + (token.length() == 1 ? 0 : number(token.substring(1)));
        }
        double value = number(token);
        if (center && token.indexOf('.') < 0) {
            value += 0.5;
        }
        return value;
    }

    private static double offset(String token) {
        return token.length() == 1 ? 0 : number(token.substring(1));
    }

    private static double number(String token) {
        try {
            return Double.parseDouble(token);
        } catch (NumberFormatException e) {
            throw new Failure("bad-arguments");
        }
    }

    private static Location localPosition(Location base, double left, double up, double forward) {
        double yaw = Math.toRadians(base.getYaw() + 90.0);
        double pitch = Math.toRadians(-base.getPitch());
        double yawCos = Math.cos(yaw);
        double yawSin = Math.sin(yaw);
        double pitchCos = Math.cos(pitch);
        double pitchSin = Math.sin(pitch);
        double upCos = Math.cos(pitch + Math.PI / 2);
        double upSin = Math.sin(pitch + Math.PI / 2);
        double[] f = {yawCos * pitchCos, pitchSin, yawSin * pitchCos};
        double[] u = {yawCos * upCos, upSin, yawSin * upCos};
        double[] l = {-(f[1] * u[2] - f[2] * u[1]), -(f[2] * u[0] - f[0] * u[2]), -(f[0] * u[1] - f[1] * u[0])};
        Location out = base.clone();
        out.setX(base.getX() + f[0] * forward + u[0] * up + l[0] * left);
        out.setY(base.getY() + f[1] * forward + u[1] * up + l[1] * left);
        out.setZ(base.getZ() + f[2] * forward + u[2] * up + l[2] * left);
        return out;
    }

    private static double angle(String token, double current) {
        if (token.startsWith("~")) {
            return current + (token.length() == 1 ? 0 : number(token.substring(1)));
        }
        return number(token);
    }

    private static void facing(Call call, Location target, int index) {
        double fx;
        double fy;
        double fz;
        if (call.token(index).equalsIgnoreCase("entity")) {
            Entity looked = call.single(index + 1);
            boolean eyes = !call.has(index + 2) || call.token(index + 2).toLowerCase(Locale.ROOT).equals("eyes");
            Location at = eyes && looked instanceof org.bukkit.entity.LivingEntity living ? living.getEyeLocation() : looked.getLocation();
            fx = at.getX();
            fy = at.getY();
            fz = at.getZ();
        } else {
            Location at = position(call, call.origin(), index);
            fx = at.getX();
            fy = at.getY();
            fz = at.getZ();
        }
        double dx = fx - target.getX();
        double dy = fy - target.getY();
        double dz = fz - target.getZ();
        target.setYaw((float) Math.toDegrees(Math.atan2(-dx, dz)));
        target.setPitch((float) -Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz))));
    }

    private static String format(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }
}
