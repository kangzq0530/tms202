package com.msemu.world.client.field.forceatoms;

/**
 * Created by Weber on 2018/5/12.
 */
public class ForceAtomHandler {
//
//    private List<AbstractForceAtom> forceAtomInfos;
//    private int nKey = 1;
//    private final Lock lock;
//
//    public ForceAtomHandler() {
//        this.forceAtomInfos = new LinkedList<>();
//        this.lock = new ReentrantLock();
//    }
//
//    public void reset() {
//        this.forceAtomInfos.clear();
//        this.nKey = 1;
//    }
//
//    public AbstractForceAtom getNewAtom(Character player, int skillID, boolean byMob) {
//        lock.lock();
//        try {
//            this.nKey++;
////            AbstractForceAtom fi = new AbstractForceAtom(player, nKey, skillID, FileTime.getTime().getLowDateTime(), byMob);
////            fi.setCount(nKey);
////            fi.setCreateTime(FileTime.getTime().getLowDateTime());
////            fi.setB
////            this.forceAtomInfos.add(fi);
//            return fi;
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    public AbstractForceAtom getAtom(int key) {
//        lock.lock();
//        try {
//            for (AbstractForceAtom atom : this.forceAtomInfos) {
//                if (atom.getCount() == key) {
//                    return atom;
//                }
//            }
//            return null;
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    public void removeExpire() {
//        lock.lock();
//        try {
//            List<Integer> toRemove = new ArrayList<>();
//            for (AbstractForceAtom atom : this.forceAtomInfos) {
//                if (!atom.isValid()) {
//                    toRemove.add(this.forceAtomInfos.indexOf(atom));
//                }
//            }
//            toRemove.forEach(i -> this.forceAtomInfos.remove((int) i));
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    public void removeAtom(int key) {
//        lock.lock();
//        try {
//            int toRemove = -1;
//            for (AbstractForceAtom atom : this.forceAtomInfos) {
//                if (atom.getKey() == key) {
//                    toRemove = this.forceAtomInfos.indexOf(atom);
//                }
//            }
//            this.forceAtomInfos.remove(toRemove);
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    public void removeAtom(AbstractForceAtom atom) {
//        lock.lock();
//        try {
//            this.forceAtomInfos.remove(atom);
//        } finally {
//            lock.unlock();
//        }
//    }
}
