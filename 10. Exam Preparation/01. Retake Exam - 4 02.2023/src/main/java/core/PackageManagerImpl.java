package core;

import models.Package;

import java.util.*;
import java.util.stream.Collectors;

public class PackageManagerImpl implements PackageManager {
    private Map<String, Package> packagesById = new LinkedHashMap<>();
    private Map<String, Package> packagesByNameAndVersion = new LinkedHashMap<>();
    private Map<String, Set<Package>> dependenciesByParentId = new LinkedHashMap<>();
    private Set<Package> independentPackages = new TreeSet<>(Comparator.comparing(
                    Package::getReleaseDate).reversed()
            .thenComparing(Package::getVersion));

    private Map<String, Package> packagesByname = new LinkedHashMap<>();

    @Override
    public void registerPackage(Package _package) {
        String nameAndVersion = _package.getName() + "-" + _package.getVersion();
        if (this.packagesByNameAndVersion.containsKey(nameAndVersion)) {
            throw new IllegalArgumentException();
        }

        this.packagesById.put(_package.getId(), _package);
        this.packagesByNameAndVersion.put(nameAndVersion, _package);
        this.independentPackages.add(_package);

        if (this.packagesByname.containsKey(_package.getName())) {
            if (_package.getName().compareTo(this.packagesByname.get(_package.getName()).getName()) > 0) {
                this.packagesByname.put(_package.getName(), _package);
            }
        } else {
            this.packagesByname.put(_package.getName(), _package);
        }
    }

    @Override
    public void removePackage(String packageId) {
        if (!this.packagesById.containsKey(packageId)) {
            throw new IllegalArgumentException();
        }

        Package _package = this.packagesById.remove(packageId);
        this.packagesByNameAndVersion.remove(_package.getName() + "-" + _package.getVersion());

        this.dependenciesByParentId.entrySet().forEach( (entry) -> {
            Set<Package> currentSet = entry.getValue();
            currentSet.remove(_package);
        });
        this.independentPackages.remove(_package);
        this.packagesByname.remove(_package.getName());
    }

    @Override
    public void addDependency(String packageId, String dependencyId) {
        if (!this.packagesById.containsKey(packageId)
                || !this.packagesById.containsKey(dependencyId)) {
            throw new IllegalArgumentException();
        }

        Package dependency = this.packagesById.get(dependencyId);
        Package bossPackage = this.packagesById.get(packageId);

        if (this.dependenciesByParentId.get(packageId) == null) {
            this.dependenciesByParentId.put(packageId, new LinkedHashSet<>());
        }

        this.dependenciesByParentId.get(packageId).add(dependency);
        this.independentPackages.remove(bossPackage);
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
        return this.independentPackages;
    }

    @Override
    public Iterable<Package> getOrderedPackagesByReleaseDateThenByVersion() {
        return this.packagesByname.values()
                .stream()
                .sorted(Comparator.comparing(Package::getReleaseDate).reversed()
                        .thenComparing(Package::getVersion))
                .collect(Collectors.toList());
    }
}
