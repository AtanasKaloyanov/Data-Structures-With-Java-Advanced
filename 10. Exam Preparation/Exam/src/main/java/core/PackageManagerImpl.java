package core;

import models.Package;

import java.util.*;

public class PackageManagerImpl implements PackageManager {
    private Map<String, Package> packagesById = new HashMap<>();

    private Map<String, Package> packagesByNameAndVersion = new HashMap<>();

    private Map<String, TreeSet<Package>> dependenciesByParentId = new LinkedHashMap<>();

    @Override
    public void registerPackage(Package _package) {
        String nameAndVersion = _package.getName() + "-" + _package.getVersion();
        if (this.packagesByNameAndVersion.containsKey(nameAndVersion)) {
            throw new IllegalArgumentException();
        }

        this.packagesById.put(_package.getId(), _package);
        this.packagesByNameAndVersion.put(nameAndVersion, _package);
    }

    @Override
    public void removePackage(String packageId) {
        if (!this.packagesById.containsKey(packageId)) {
            throw new IllegalArgumentException();
        }

        Package _package = this.packagesById.remove(packageId);
        this.packagesByNameAndVersion.remove(_package.getName() + "-" + _package.getVersion());
        this.dependenciesByParentId.put(packageId, new TreeSet<>(Comparator.comparing(Package::getReleaseDate).reversed()
                .thenComparing(Package::getVersion)));
    }

    @Override
    public void addDependency(String packageId, String dependencyId) {
        if (!this.packagesById.containsKey(packageId)
                || !this.packagesById.containsKey(dependencyId)) {
            throw new IllegalArgumentException();
        }

        if (this.dependenciesByParentId.get(packageId) == null) {
            this.dependenciesByParentId.put(packageId, new TreeSet<>(
                    Comparator.comparing(Package::getReleaseDate).reversed()
                    .thenComparing(Package::getVersion)));
        }
        Package dependency = this.packagesById.get(dependencyId);
        this.dependenciesByParentId.get(packageId).add(dependency);
    }

    @Override
    public boolean contains(Package _package) {
        return this.packagesById.containsKey(_package.getId());
    }

    @Override
    public int size() {
        return this.packagesById.size();
    }

    @Override
    public Iterable<Package> getDependants(Package _package) {
        return this.dependenciesByParentId.get(_package.getId());
    }

    @Override
    public Iterable<Package> getIndependentPackages() {
        return null;
    }

    @Override
    public Iterable<Package> getOrderedPackagesByReleaseDateThenByVersion() {
        return null;
    }
}
