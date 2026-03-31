import os
import re

domain_dir = 'domain/src/main/java/com/eyther/lumbridge/domain/repository'

for root, _, files in os.walk(domain_dir):
    for file in files:
        if not file.endswith('Repository.kt'):
            continue

        filepath = os.path.join(root, file)

        with open(filepath, 'r') as f:
            content = f.read()

        package_match = re.search(r'package\s+(.*)', content)
        if not package_match: continue
        domain_package = package_match.group(1)
        data_package = domain_package.replace('domain.repository', 'data.repository')

        class_match = re.search(r'class\s+([A-Za-z0-9_]+)', content)
        if not class_match: continue
        class_name = class_match.group(1)
        impl_name = f"{class_name}Impl"

        data_dir = root.replace('domain/src/main/java/com/eyther/lumbridge/domain', 'data/src/main/java/com/eyther/lumbridge/data')
        os.makedirs(data_dir, exist_ok=True)
        data_filepath = os.path.join(data_dir, f"{impl_name}.kt")

        impl_content = content.replace(f'package {domain_package}', f'package {data_package}')

        # Add import for domain interface
        import_str = f"import {domain_package}.{class_name}\n"
        first_import_pos = impl_content.find("import ")
        if first_import_pos != -1:
            impl_content = impl_content[:first_import_pos] + import_str + impl_content[first_import_pos:]

        impl_content = re.sub(
            r'class\s+([A-Za-z0-9_]+)(\s*@Inject\s*constructor\s*\((?:[^)]*)\))?\s*\{',
            f'class {impl_name}\\2 : {class_name} {{',
            impl_content, flags=re.DOTALL
        )

        lines = impl_content.split('\n')
        inside_class = False
        for i, line in enumerate(lines):
            if f'class {impl_name}' in line:
                inside_class = True

            if inside_class:
                if 'private ' in line:
                    continue
                if re.match(r'^\s*val\s+', line):
                    lines[i] = line.replace('val ', 'override val ', 1)
                elif re.match(r'^\s*fun\s+', line):
                    lines[i] = line.replace('fun ', 'override fun ', 1)
                elif re.match(r'^\s*suspend fun\s+', line):
                    lines[i] = line.replace('suspend fun ', 'override suspend fun ', 1)

        with open(data_filepath, 'w') as f:
            f.write('\n'.join(lines))
