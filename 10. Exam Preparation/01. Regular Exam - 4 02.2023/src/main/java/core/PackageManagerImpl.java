package core;

import models.Package;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class PackageManagerImpl implements PackageManager {
    private final Map<String, Package> packageById;

    private final Set<Package> packages;

    private final Map<Package, List<Package>> dependencies;

    public PackageManagerImpl() {
        this.packageById = new LinkedHashMap<>();

        this.packages = new LinkedHashSet<>();

        this.dependencies = new LinkedHashMap<>();
    }

    @Override
    public void registerPackage(Package _package) {
        if (!this.packages.isEmpty()) {
            String name = _package.getName();
            String version = _package.getVersion();

            List<Package> collect = this.packages
                    .stream()
                    .filter(p -> p.getName().equals(name)
                            && p.getVersion().equals(version))
                    .collect(Collectors.toList());

            if (collect.size() != 0) {
                throw new IllegalArgumentException();
            }
        }

        this.packageById.put(_package.getId(), _package);
        this.packages.add(_package);
    }

    @Override
    public void removePackage(String packageId) {
        Package result = this.packageById.remove(packageId);

        if (result == null) {
            throw new IllegalArgumentException();
        }

        this.packages.remove(result);
        this.dependencies.remove(result);
    }

    @Override
    public void addDependency(String packageId, String dependencyId) {
        Package mainPack = this.packageById.get(packageId);
        Package depPack = this.packageById.get(dependencyId);
        if (mainPack == null || depPack == null) {
            throw new IllegalArgumentException();
        }

        if (!this.dependencies.containsKey(mainPack)) {
            this.dependencies.put(mainPack, new ArrayList<>());
        }

        List<Package> packages = this.dependencies.get(mainPack);
        packages.add(depPack);
        this.dependencies.put(mainPack, packages);

    }

    @Override
    public boolean contains(Package _package) {
        return this.packages.contains(_package);
    }

    @Override
    public int size() {
        return this.packages.size();
    }

    @Override
    public Iterable<Package> getDependants(Package _package) {
        List<Package> packages = this.dependencies.get(_package);

        if (packages == null) {
            return Collections.emptyList();
        }

        return packages;
    }

    @Override
    public Iterable<Package> getIndependentPackages() {
        return this.packages.stream()
                .filter(p -> {
                    return !this.dependencies.containsKey(p);
                })
                .sorted((l, r) -> {
                    LocalDateTime leftReleaseDate = l.getReleaseDate();
                    LocalDateTime rightReleaseDate = r.getReleaseDate();


                    if (leftReleaseDate != rightReleaseDate) {
                        return rightReleaseDate.compareTo(leftReleaseDate);
                    }

                    return l.getVersion().compareTo(r.getVersion());
                })
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Package> getOrderedPackagesByReleaseDateThenByVersion() {
        return this.packageById.values()
                .stream()
                .sorted((l, r) -> {
                    LocalDateTime leftDate = l.getReleaseDate();
                    LocalDateTime rightDate = r.getReleaseDate();

                    if (leftDate != rightDate) {
                        return rightDate.compareTo(leftDate);
                    } else {
                        return l.getVersion().compareTo(r.getVersion());
                    }
                })
                .collect(Collectors.toList());
    }
}
