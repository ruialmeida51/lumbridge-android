import re
import os

domain_dir = 'domain/src/main/java/com/eyther/lumbridge/domain/repository'

for root, _, files in os.walk(domain_dir):
    for file in files:
        if not file.endswith('Repository.kt'): continue
        filepath = os.path.join(root, file)

        with open(filepath, 'r') as f:
            content = f.read()

        package_match = re.search(r'package\s+(.*)', content)
        if not package_match: continue
        package = package_match.group(1)

        class_match = re.search(r'class\s+([A-Za-z0-9_]+)', content)
        if not class_match: continue
        class_name = class_match.group(1)

        imports = []
        for line in content.split('\n'):
            if line.startswith('import '):
                if 'data.datasource' in line or 'data.model' in line or 'mapper.toCached' in line:
                    continue
                imports.append(line)

        class_content = re.sub(r'^.*?class ' + class_name + r'.*?\{', '', content, flags=re.DOTALL)
        class_content = class_content.rsplit('}', 1)[0]

        signatures = []
        for prop in re.finditer(r'^\s*(val\s+[a-zA-Z0-9_]+.*?)\s*=', class_content, re.MULTILINE):
            if 'private ' not in prop.group(0):
                signatures.append("    " + prop.group(1).strip())

        def extract_functions(text):
            funcs = []
            i = 0
            while i < len(text):
                fun_idx = text.find('fun ', i)
                if fun_idx == -1: break

                line_start = text.rfind('\n', 0, fun_idx)
                if line_start != -1 and 'private ' in text[line_start:fun_idx]:
                    i = fun_idx + 4
                    continue

                is_suspend = text.rfind('suspend ', line_start, fun_idx) != -1

                paren_open = text.find('(', fun_idx)
                if paren_open == -1: break

                count = 1
                j = paren_open + 1
                while j < len(text) and count > 0:
                    if text[j] == '(': count += 1
                    elif text[j] == ')': count -= 1
                    j += 1

                end_idx = j
                while end_idx < len(text) and text[end_idx] not in ('{', '='):
                    end_idx += 1

                sig = text[fun_idx:end_idx].strip()
                if is_suspend: sig = 'suspend ' + sig
                sig = re.sub(r'\s+', ' ', sig)
                funcs.append("    " + sig)

                i = end_idx
            return funcs

        func_sigs = extract_functions(class_content)
        signatures.extend(func_sigs)

        interface_content = f"package {package}\n\n"
        interface_content += "\n".join(imports) + "\n\n"
        interface_content += f"interface {class_name} {{\n"
        interface_content += "\n".join(signatures) + "\n"
        interface_content += "}\n"

        with open(filepath, 'w') as f:
            f.write(interface_content)
